package org.firstinspires.ftc.teamcode.utils;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ShooterFPT2 {
    private LimelightHardware limelight;
    public final DcMotorEx MShooter1, MShooter2;
    public final DcMotorEx MTurnOuttake;
    public Servo SAngle;
    double SAngleLowest = 0.8492, SBackKickOff = 0, SBackKickOn = 0.9;
    double SGate1Close = 0.03, SGate1Open = 0.182, SGate2Close = 0.03, SGate2Open = 0.182;
    public double P = 18, I = 0.2, D = 0.05, F = 0.5;
    boolean MTurnOuttakeReverse = false;
    volatile boolean isBusy = false;
    int tprShot = 1;
    boolean overwriteShoot;
    int FLYWHEEL_VELOCITY_GAIN_DURATION = 200;
    int THREE_BALLS_SHOOTING_DURATION = 1000;
    int MIN_TPR = 1450
            ;
//    private final DistanceSensor DSensor;
    private final IntakeFPT2 intake;
    boolean isFull = false;
    ElapsedTime timer = new ElapsedTime();
    double lastError = 0;
    double integralSum = 0;
    long lastTime = System.nanoTime();
    int maxTick = 670, minTick = -621;

    public ShooterFPT2(HardwareMap hardwareMap, IntakeFPT2 intake) {
        MShooter1 = hardwareMap.get(DcMotorEx.class, "m6");
        MShooter2 = hardwareMap.get(DcMotorEx.class, "m7");
        MShooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        MTurnOuttake = hardwareMap.get(DcMotorEx.class, "m5");
        MTurnOuttake.setDirection(DcMotorSimple.Direction.REVERSE);
        MTurnOuttake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MTurnOuttake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MTurnOuttake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        MShooter2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        SAngle = hardwareMap.get(Servo.class, "s4");
//        SAngle.setDirection(Servo.Direction.REVERSE);
//        SAngle.setPosition(SAngleLowest);
//
//        SGate1 = hardwareMap.get(Servo.class, "s10");
//        SGate2 = hardwareMap.get(Servo.class, "s11");
//        SBackKick = hardwareMap.get(Servo.class, "s9");
//        resetShooterServos();

        limelight = new LimelightHardware(hardwareMap);
//        DSensor = hardwareMap.get(DistanceSensor.class, "ds");
        this.intake = intake;
    }

//    public void checkTunnelFull() {
//        if(isBusy || !intake.isActive()) return;
//
//        double distance = DSensor.getDistance(DistanceUnit.CM);
//
//        if (distance < 5) {
//            if(timer.seconds() > 2 && !isFull) {
//                isFull = true;
//                intake.stop();
////                setMotorVelocity(MIN_TPR);
//            }
//        } else if (distance > 5) {
//            isFull = false;
//            timer.reset();
//        }
//    }

    @SuppressLint("SuspiciousIndentation")
    public void shoot(Telemetry telemetry) throws InterruptedException {
        double distance = 150;
        if (limelight.getAprilTagData(telemetry) != null)
            distance = limelight.getAprilTagData(telemetry).z;
            telemetry.addData("distance", distance);
            if (distance <= 115) {
                tprShot = MIN_TPR;
                SAngle.setPosition(calculateAngle(distance, 0.15));
            } else if (distance <= 240) {
                SAngle.setPosition(calculateAngle(distance, 0.08));
                tprShot = 1675;
            } else {
                tprShot = 2700;
                SAngle.setPosition(calculateAngle(distance,0.05));
            }
            telemetry.addData("error", tprShot-MShooter1.getVelocity());
            setMotorVelocity(tprShot);
        }

    //    private void resetShooterServos() {
    //        SBackKick.setPosition(SBackKickOff);
    //        SGate1.setPosition(SGate1Close);
    //        SGate2.setPosition(SGate2Close);
    //    }
    //
    //    public void toggleFlywheel() {
    //        int maxShooterVelocity = 2200;
    //        if(!overwriteShoot) {
    //            MShooter1.setVelocity(maxShooterVelocity);
    //            MShooter2.setVelocity(maxShooterVelocity);
    //        } else {
    //            MShooter1.setVelocity(0);
    //            MShooter2.setVelocity(0);
    //        }
    //        overwriteShoot = !overwriteShoot;
    //    }
    //
        public void setMotorVelocity(int velocity){
            MShooter1.setVelocity(velocity);
            MShooter2.setVelocity(velocity);
        }

    //
        public double calculateAngle(double dis, double offset){
            double a = -1.5015e-05;
            double b =  0.0064733;
            double c = -0.0007912;
//            double offset = 0.085;

            double pos = (a * dis * dis + b * dis + c) - offset;
            return Math.max(0.0, Math.min(1.0, pos));
        }

    public boolean isBusy() {
        return false;
    }

//    public boolean isBusy() {
//        return false;
//    }

    public void holdShooter(int id, Telemetry telemetry, boolean reverseMotor){
        if(reverseMotor) MTurnOuttake.setDirection(DcMotorSimple.Direction.REVERSE);
        else MTurnOuttake.setDirection(DcMotorSimple.Direction.FORWARD);
        ApriltagData data = limelight.getAprilTagData(telemetry);

        if(data != null && MTurnOuttake.getCurrentPosition() > minTick && MTurnOuttake.getCurrentPosition() < maxTick){
            if(data.id == id) {
                MTurnOuttake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                double Tx = data.x+9;
                long now = System.nanoTime();
                double dt = (now - lastTime) / 1e9;
                if (dt <= 0) dt = 0.02;
                lastTime = now;
                integralSum += Tx * dt;
                integralSum = Math.max(-5, Math.min(5, integralSum));
                double derivative = (Tx - lastError) / dt;
                double output = 0.000015 * Tx
                        + 0.0000125 * integralSum
                        + 0.000025 * derivative;
                lastError = Tx;
                output += Math.copySign(0.07, output);
                if (Math.abs(Tx) > 3) {
                    MTurnOuttake.setPower(Math.copySign(Math.max(0, Math.min(Math.abs(output), 0.3)), output));
                    telemetry.addData("output", output);
                } else {
//                    MTurnOuttake.setPower(Math.copySign(0.06 * 0.6, output));
                    MTurnOuttake.setPower(0);
                }
                double current = MTurnOuttake.getCurrent(CurrentUnit.AMPS);
                if (current > 7) {
                    MTurnOuttake.setPower(0);
                }
                telemetry.addLine("---------------------------");
                telemetry.addData("Tx", Tx);
                telemetry.addData("distance", data.z);
            } else {
                MTurnOuttake.setPower(0);
            }
        } else {
            MTurnOuttake.setPower(0);
        }
        telemetry.addData("encoder", MTurnOuttake.getCurrentPosition());
    }
}
