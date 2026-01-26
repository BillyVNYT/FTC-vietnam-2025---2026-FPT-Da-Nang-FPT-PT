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
//    private final LimelightHardware limelight;
    double P = 6;
    double I = 0;
    double D = 1;
    double F = 3;
    double[][] hoodTable = {
            {93.0,  0.6922},
            {111.0, 0.8344},
            {121.0, 0.9089},
            {133.0, 0.9267},
            {141.0, 0.9606},
            {154.0, 1.0},
            {161.0, 1.0}
    };
    double[] servoPositions = {0.8492, 0.6389, 0};
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

    public Shooter(HardwareMap hardwareMap) {
        MShooter1 = hardwareMap.get(DcMotorEx.class, "m0");
        MShooter2 = hardwareMap.get(DcMotorEx.class, "m1");
        MShooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        MTurnOuttake = hardwareMap.get(DcMotorEx.class, "m4");
        MTurnOuttake.setDirection(DcMotorSimple.Direction.REVERSE);
        MTurnOuttake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        SAngle = hardwareMap.get(Servo.class, "s3");
        SAngle.setDirection(Servo.Direction.REVERSE);

        SLoaderUp1 = hardwareMap.get(ServoImplEx.class, "s10");
        SLoaderUp2 = hardwareMap.get(ServoImplEx.class, "s11");

        SLoaderOut = hardwareMap.get(Servo.class, "s9");
        SLoaderOut.setPosition(SLoaderOutHiddenPos);

        limelight = new LimelightHardware(hardwareMap);
        limelight.changePipeline(0);
    }

    int FLYWHEEL_VELOCITY_GAIN_DURATION = 500;

    public void shoot(int count, SortBall spindexer, Telemetry telemetry) throws InterruptedException {
        isBusy = true;

        // 1. Kiểm tra Null an toàn cho Limelight
        ApriltagData data = limelight.getAprilTagData(telemetry);
        double distance = (data != null) ? data.z : 130.0; // Khoảng cách mặc định nếu mất dấu

        // Tính toán góc và vận tốc
        tprShot = (distance <= 100) ? 1000 : (distance <= 240) ? 1500 : 2300;
        SAngle.setPosition(calculateAngle(distance, spindexer.is_lastBall, telemetry));
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
        int maxShooterVelocity = 2200;
        if(!overwriteShoot) {
            MShooter1.setVelocity(maxShooterVelocity);
            MShooter2.setVelocity(maxShooterVelocity);
        } else {
            MShooter1.setVelocity(0);
            MShooter2.setVelocity(0);
        }
        overwriteShoot = !overwriteShoot;
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
    }

    public boolean isBusy(){
        return isBusy;
    }

    public void updateOuttakeAngle(double rx){
        MTurnOuttake.setPower(rx);
    }
    public boolean HoldShooter(int id, Telemetry telemetry, boolean reverseMotor) {

        MTurnOuttake.setDirection(
                reverseMotor ? DcMotorSimple.Direction.REVERSE
                        : DcMotorSimple.Direction.FORWARD
        );

        limelight.changePipeline(0);
        ApriltagData data = limelight.getAprilTagData(telemetry);
        boolean locked = false;

        telemetry.addData("MotorCurrent", MTurnOuttake.getCurrent(CurrentUnit.AMPS));

        if (data != null) {
            double error = data.x; // Tx
            long now = System.nanoTime();

            double dt = (lastTime == 0) ? 0 : (now - lastTime) / 1e9;
            lastTime = now;

            // Deadband để tránh rung
            if (Math.abs(error) < 0.5) {
                integral = 0;
                MTurnOuttake.setPower(0);
                locked = true;
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

                MTurnOuttake.setPower(
                        MTurnOuttakeReverse ? -output : output
                );
            }

            telemetry.addData("Tx", error);
            telemetry.addData("distance", data.z);
            telemetry.addData("PID Out", lastError);

            telemetry.addData("curTargetVelocity", 2600);
            telemetry.addData("error", error);
            telemetry.addLine("---------------------------");
            telemetry.addData("distance", data.z);
            telemetry.update();

            return locked;
        } else {
            // Mất tag → giữ nguyên hoặc dừng
            integral = 0;
            lastError = 0;
            MTurnOuttake.setPower(0);
            telemetry.addLine("No AprilTag detected");
            return false;
        }

    }
    public double calculateAngle(double dis, boolean is_lastBall, Telemetry telemetry){
//        if (dis <= hoodTable[0][0]) return hoodTable[0][1];
//        // Nếu khoảng cách lớn hơn điểm cao nhất
//        if (dis >= hoodTable[hoodTable.length - 1][0]) return hoodTable[hoodTable.length - 1][1];
//
//        for (int i = 0; i < hoodTable.length - 1; i++) {
//            if (dis >= hoodTable[i][0] && dis <= hoodTable[i+1][0]) {
//                // Công thức nội suy tuyến tính: y = y1 + (x - x1) * (y2 - y1) / (x2 - x1)
//                double x1 = hoodTable[i][0];
//                double y1 = hoodTable[i][1];
//                double x2 = hoodTable[i+1][0];
//                double y2 = hoodTable[i+1][1];
//
//                return (y1 + (dis - x1) * (y2 - y1) / (x2 - x1))-0.15;
//            }
//        }
//        return 0.95;
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
