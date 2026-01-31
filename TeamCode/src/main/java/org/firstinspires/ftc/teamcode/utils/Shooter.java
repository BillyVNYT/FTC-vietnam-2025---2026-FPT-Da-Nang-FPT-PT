package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Shooter {
    public LimelightHardware limelight;
    public final DcMotorEx MShooter1, MShooter2;
    public final DcMotorEx MTurnOuttake;
    public Servo SAngle;
    private final Servo SLoaderOut;
    private final ServoImplEx SLoaderUp1, SLoaderUp2;

    double servoAtLowZone = 0.45;
    public double P = 1.6;
    public double I = 0.0001;
    public double D = 0.02;
    public double F = 11.1859;
    double SLoaderOutHiddenPos = 0.03;
    double SLoaderOutVisiblePos = 0.182;
    int maxTurnOuttakePos = 424;
    int minTurnOuttakePos = -424;

    volatile boolean isBusy = false;

    int tprShot = 1;
    boolean overwriteShoot = false;
    double kP = 0.015;
    double kI = 0.0000125;
    double kD = 0.000025;

    // PID state
    double integral = 0;
    double lastError = 0;
    long lastTime = 0;

    Telemetry telemetry;
    boolean isRed;

    public Shooter(HardwareMap hardwareMap, boolean holdOuttake, Telemetry telemetry, boolean isRed) {
        MShooter1 = hardwareMap.get(DcMotorEx.class, "m0");
        MShooter2 = hardwareMap.get(DcMotorEx.class, "m1");
        MShooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        MTurnOuttake = hardwareMap.get(DcMotorEx.class, "m4");
        MTurnOuttake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MTurnOuttake.setDirection(DcMotorSimple.Direction.REVERSE);

        if(holdOuttake) {
            MTurnOuttake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            MTurnOuttake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            MTurnOuttake.setTargetPosition(isRed ? 169 : -169);
            MTurnOuttake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            MTurnOuttake.setPower(1);
        }

        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        MShooter2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        SAngle = hardwareMap.get(Servo.class, "s3");
        SAngle.setDirection(Servo.Direction.REVERSE);
        SAngle.setPosition(servoAtLowZone);

        SLoaderUp1 = hardwareMap.get(ServoImplEx.class, "s10");
        SLoaderUp2 = hardwareMap.get(ServoImplEx.class, "s11");

        SLoaderOut = hardwareMap.get(Servo.class, "s9");
        SLoaderOut.setPosition(SLoaderOutHiddenPos);

        limelight = new LimelightHardware(hardwareMap);
        limelight.changePipeline(0);

        this.telemetry = telemetry;
        this.isRed = isRed;
    }

    String[] pidf = {"p", "i", "d", "f"};
    int curTuneIdx = 0;
    public String goToNextPidf() {
        curTuneIdx = (curTuneIdx + 1) % pidf.length;
        return pidf[curTuneIdx];
    }

    public void updateMTurnOuttakePos(int pos) {
        MTurnOuttake.setTargetPosition(pos);
    }

    public double[] tunePidf(double nextI, Telemetry telemetry) {
        if(curTuneIdx == 0) {
            P += nextI;
        }
        else if(curTuneIdx == 1) {
            I += nextI;
        }
        else if(curTuneIdx == 2) {
            D += nextI;
        }
        else if(curTuneIdx == 3) {
            F += nextI;
        }

        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        MShooter2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        double[] arr = {P, I, D, F};
        return arr;

    }

    int FLYWHEEL_VELOCITY_GAIN_DURATION = 500;

    public double[] getVelocity() {
        double[] arr = {MShooter1.getVelocity(), MShooter2.getVelocity()};
        return arr;
    }

    public void shoot(int count, SortBall spindexer, int overridedVelocity, boolean fixedAngle) throws InterruptedException {
        isBusy = true;
        overwriteShoot = false;

        // 1. Kiểm tra Null an toàn cho Limelight
        ApriltagData data = limelight.getAprilTagData(telemetry);
        double distance = (data != null) ? data.z : 130.0; // Khoảng cách mặc định nếu mất dấu
        telemetry.addData("distance", distance);

        tprShot = (distance <= 100) ? 1000 : (distance <= 240) ? 1900 : 2400;
        telemetry.addData("tprShot", tprShot);

        if(overridedVelocity > 0) {
            tprShot = overridedVelocity;
            if(fixedAngle) SAngle.setPosition(servoAtLowZone);
        } else {
            double angle = calculateAngle(distance, spindexer.is_lastBall);
            SAngle.setPosition(angle);
            telemetry.addData("angle", angle);
        }

        setMotorVelocity(tprShot);

        Thread.sleep(FLYWHEEL_VELOCITY_GAIN_DURATION);

        SLoaderOut.setPosition(SLoaderOutVisiblePos);
        Thread.sleep(500);

        // 2. Thread phụ an toàn hơn
        Thread servoToggler = new Thread(() -> {
            try {
                boolean toggleStatus = true;
                while (isBusy && !Thread.currentThread().isInterrupted()) {
                    if (toggleStatus) {
                        SLoaderUp1.setPosition(0.1);
                        SLoaderUp2.setPosition(0.1);
                    }
                    else {
                        SLoaderUp1.setPosition(0.0);
                        SLoaderUp2.setPosition(0.0);
                    }
                    toggleStatus = !toggleStatus;
                    Thread.sleep(100);
                }

            } catch (InterruptedException e) {}

            SLoaderUp1.setPosition(0.0);
            SLoaderUp2.setPosition(0.0);
        });

        servoToggler.start();
        spindexer.spinToShooter(count, telemetry);

//        // 3. Kết thúc thread đúng cách
        isBusy = false;
        servoToggler.join(); // Chờ thread phụ kết thúc hẳn
        servoToggler = null;

        setMotorVelocity(0);
        SLoaderOut.setPosition(SLoaderOutHiddenPos);
    }

    public void toggleFlywheel() {
        if(MShooter1.getVelocity() < 1000) {
            MShooter1.setVelocity(2000);
            MShooter2.setVelocity(2000);
            overwriteShoot = true; // Đồng bộ lại biến
            telemetry.addLine("Flywheel: ON");
        } else {
            MShooter1.setVelocity(0);
            MShooter2.setVelocity(0);
            overwriteShoot = false; // Đồng bộ lại biến
            telemetry.addLine("Flywheel: OFF");
        }
    }

    public void setMotorVelocity(int velocity){
        MShooter1.setVelocity(velocity);
        MShooter2.setVelocity(velocity);

        double curVelocity = MShooter1.getVelocity();
        double error = velocity - curVelocity;

        telemetry.addData("curTargetVelocity", velocity);
        telemetry.addData("curVelocity", curVelocity);
        telemetry.addData("error", error);
        telemetry.addLine("---------------------------");
    }

    public boolean isBusy(){
        return isBusy;
    }

    public void updateOuttakeAngle(){
        MTurnOuttake.setPower(MTurnOuttakePower);
    }

    double limelightX, limeLightZ, limelightId, MTurnOuttakePower=0;

    public void printShooterData() {
        telemetry.addData("shooter tx error", limelightX);
        telemetry.addData("z", limeLightZ);
        telemetry.addData("id", limelightId);
        telemetry.addData("output", MTurnOuttakePower);
    }

    public void setMTurnOuttakePower(double MTurnOuttakePower) {
        this.MTurnOuttakePower = MTurnOuttakePower;
    }

    private void resetHoldShooterParam() {
        integral = 0;
        lastError = 0;
        lastTime = 0;
    }

    public void holdShooter(int id) {
        ApriltagData data = limelight.getAprilTagData(telemetry);

        if (data != null) {
            if (data.id != id) {
                return;
            }

            limeLightZ = data.z;
            limelightId = data.id;
            limelightX = data.x-(isRed ? 3 : 7); // Tx

            long now = System.nanoTime();

            int turnOuttakePos = MTurnOuttake.getTargetPosition();

            double dt = (lastTime == 0) ? 0 : (now - lastTime) / 1e9;
            lastTime = now;

            if (Math.abs(limelightX) < 0.5) {
                integral = 0;
                MTurnOuttakePower = 0;
            } else {
                // Integral
                integral += limelightX * dt;
                integral = Math.max(-10, Math.min(10, integral)); // anti-windup

                // Derivative
                double derivative = (dt > 0) ? (limelightX - lastError) / dt : 0;
                lastError = limelightX;

                // PIDF output
                double tempPower =
                        kP * limelightX +
                                kI * integral +
                                kD * derivative;

                if(turnOuttakePos >= maxTurnOuttakePos && tempPower > 0) {
                    tempPower = 0;
                    resetHoldShooterParam();
                }
                else if(turnOuttakePos <= minTurnOuttakePos && tempPower < 0){
                    tempPower = 0;
                    resetHoldShooterParam();
                }

                // Giới hạn công suất
                MTurnOuttakePower = Math.max(-0.6, Math.min(0.6, tempPower));
            }

        } else {
            // Mất tag → giữ nguyên hoặc dừng
            resetHoldShooterParam();
            MTurnOuttakePower = 0;
        }
    }
    public double calculateAngle(double dis, boolean is_lastBall){
        double a =  -0.635812;
        double b =  0.02004009;
        double c = 0.00006141738;

        double offset = 0.28;

        double pos = -0.635812 + 0.02004009 * dis - 0.00006141738 * Math.pow(dis, 2);

        return Math.max(0.0, Math.min(1.0, pos))-offset;
    }

//    double angleFormula(double distance, int tpr) {
//        double velocity = (double) tpr / 28 * 2 * Math.PI * 0.04;
//        telemetry.addData("velocity", velocity);
//        double d = distance/100;
//        double g = 9.81;
//        double h = 1;
//        double tanAngle = (Math.pow(velocity,2) - Math.sqrt(Math.pow(velocity,4) - g * (g*Math.pow(d,2) + 2*h*Math.pow(velocity,2)))) / (g*d);
//        double angle = Math.toDegrees(Math.atan(tanAngle));
//        double servoAngle = 90 - angle;
//        telemetry.addData("angle", angle);
//        double servoPos = 0.04*servoAngle-1.4;
//        telemetry.addData("servoPos", servoPos);
//        return servoPos;
//    }

    public void updateServoAngle(double degree){
        SAngle.setPosition(0.03638079*degree - 0.8736323);
    }
}
