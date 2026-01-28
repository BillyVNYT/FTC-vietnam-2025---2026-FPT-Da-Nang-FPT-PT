package org.firstinspires.ftc.teamcode.utils;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;


public class Shooter {
    public LimelightHardware limelight;
    public final DcMotorEx MShooter1, MShooter2;
    public final DcMotorEx MTurnOuttake;
    public Servo SAngle;
    private final Servo SLoaderOut;
    private final ServoImplEx SLoaderUp1, SLoaderUp2;

    double servoAtLowZone = 0.45;
    double P = 63.36;
    double I = 0.53;
    double D = 0.06;
    double F = 0.01;
    double SLoaderOutHiddenPos = 0.03;
    double SLoaderOutVisiblePos = 0.182;
    boolean MTurnOuttakeReverse = false;
    int maxTurnShooter = 1200;
    int minTurnShooter = 0;

    volatile boolean isBusy = false;

    int tprShot = 1;
    boolean overwriteShoot;
    double kP = 0.065;
    double kI = 0.0001;
    double kD = 0.015;
    double kF = 0;

    // PID state
    double integral = 0;
    double lastError = 0;
    long lastTime = 0;

    public Shooter(HardwareMap hardwareMap, boolean holdOuttake) {
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
            MTurnOuttake.setTargetPosition(169);
            MTurnOuttake.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            MTurnOuttake.setPower(1);
        }

        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        MShooter2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        SAngle = hardwareMap.get(Servo.class, "s3");
        SAngle.setDirection(Servo.Direction.REVERSE);

        SLoaderUp1 = hardwareMap.get(ServoImplEx.class, "s10");
        SLoaderUp2 = hardwareMap.get(ServoImplEx.class, "s11");

        SLoaderOut = hardwareMap.get(Servo.class, "s9");
        SLoaderOut.setPosition(SLoaderOutHiddenPos);

        limelight = new LimelightHardware(hardwareMap);
        limelight.changePipeline(0);
    }

    String[] pidf = {"p", "i", "d", "f"};
    int curTuneIdx = 0;
    public String goToNextPidf() {
        curTuneIdx = (curTuneIdx + 1) % pidf.length;
        return pidf[curTuneIdx];
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


    public void shoot(int count, SortBall spindexer, Telemetry telemetry, int overridedVelocity) throws InterruptedException {
        isBusy = true;

        // 1. Kiểm tra Null an toàn cho Limelight
        ApriltagData data = limelight.getAprilTagData(telemetry);
        double distance = (data != null) ? data.z : 130.0; // Khoảng cách mặc định nếu mất dấu

        // Tính toán góc và vận tốc
        tprShot = (distance <= 100) ? 1000 : (distance <= 240) ? 1500 : 2300;

        if(overridedVelocity > 0) {
            tprShot = overridedVelocity;
            SAngle.setPosition(servoAtLowZone);
        } else {
            SAngle.setPosition(calculateAngle(distance, spindexer.is_lastBall, telemetry));
        }

        setMotorVelocity(tprShot, telemetry);

        Thread.sleep(FLYWHEEL_VELOCITY_GAIN_DURATION);

        SLoaderOut.setPosition(SLoaderOutVisiblePos);
        Thread.sleep(500);

        // 2. Thread phụ an toàn hơn
        Thread servoToggler = new Thread(() -> {
            try {
                while (isBusy && !Thread.currentThread().isInterrupted()) {
                    SLoaderUp1.setPosition(0.0);
                    SLoaderUp2.setPosition(0.0);
                    Thread.sleep(100); // Cho Servo có thời gian di chuyển
                    SLoaderUp1.setPosition(0.1);
                    SLoaderUp2.setPosition(0.1);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        servoToggler.start();
        spindexer.spinToShooter(count, telemetry);

        // 3. Kết thúc thread đúng cách
        isBusy = false;
        servoToggler.join(500); // Chờ thread phụ kết thúc hẳn

        setMotorVelocity(0, telemetry);
        SLoaderOut.setPosition(SLoaderOutHiddenPos);
    }

    public void toggleFlywheel(Telemetry telemetry) {
        if(MShooter1.getVelocity() < 10) {
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

    public void setMotorVelocity(int velocity, Telemetry telemetry){
        MShooter1.setVelocity(velocity);
        MShooter2.setVelocity(velocity);

        double curVelocity = MShooter1.getVelocity();
        double error = velocity - curVelocity;

        telemetry.addData("curTargetVelocity", velocity);
        telemetry.addData("curVelocity", curVelocity);
        telemetry.addData("error", error);
        telemetry.addLine("---------------------------");
//        telemetry.update();
    }

    public boolean isBusy(){
        return isBusy;
    }

    public void updateOuttakeAngle(double rx){
        MTurnOuttake.setPower(rx);
    }
    public void HoldShooter(int id, Telemetry telemetry) {
        ApriltagData data = limelight.getAprilTagData(telemetry);

        int currentPos = MTurnOuttake.getCurrentPosition();

        if (data != null) {
            if (data.id != id) {
                return;
            }

            double error = data.x; // Tx
            long now = System.nanoTime();

            double dt = (lastTime == 0) ? 0 : (now - lastTime) / 1e9;
            lastTime = now;

            if (Math.abs(error) < 0.5) {
                integral = 0;
                MTurnOuttake.setPower(0);
            } else {
                // Integral
                integral += error * dt;
                integral = Math.max(-20, Math.min(20, integral)); // anti-windup

                // Derivative
                double derivative = (dt > 0) ? (error - lastError) / dt : 0;
                lastError = error;

                // PIDF output
                double output =
                        kP * error +
                                kI * integral +
                                kD * derivative +
                                kF * Math.signum(error);

                // Giới hạn công suất
                output = Math.max(-0.6, Math.min(0.6, output));

                double finalPower = MTurnOuttakeReverse ? -output : output;

                // Chặn trên: Nếu vượt maxTurnShooter (1200) và vẫn muốn quay dương
                if (currentPos >= maxTurnShooter && finalPower > 0) {
                    finalPower = 0;
                    integral = 0;
                    telemetry.addLine("!!! LIMIT REACHED: MAX !!!");
                }
                else if (currentPos <= minTurnShooter && finalPower < 0) {
                    finalPower = 0;
                    integral = 0;
                    telemetry.addLine("!!! LIMIT REACHED: MIN !!!");
                }

                MTurnOuttake.setPower(finalPower);
            }

        } else {
            // Mất tag → giữ nguyên hoặc dừng
            integral = 0;
            lastError = 0;
            MTurnOuttake.setPower(0);
        }
    }
    public double calculateAngle(double dis, boolean is_lastBall, Telemetry telemetry){
        double a =  -0.635812;
        double b =  0.02004009;
        double c = 0.00006141738;

        double offset = 0.275; // chỉnh cao lên
        telemetry.update();

        double pos = -0.635812 + 0.02004009 * dis - 0.00006141738 * Math.pow(dis, 2);

        return Math.max(0.0, Math.min(1.0, pos))-offset;
    }
//    double angleFormula(double distance, int tpr, Telemetry telemetry) {
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
//        telemetry.update();
//        return servoPos;
//    }
    public void updateServoAngle(double degree, Telemetry telemetry){
        SAngle.setPosition(0.03638079*degree - 0.8736323);
    }
}
