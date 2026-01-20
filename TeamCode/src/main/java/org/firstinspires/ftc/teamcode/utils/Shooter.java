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


public class Shooter {
    public final DcMotorEx MShooter1, MShooter2;
//    private final DcMotor MTurnOuttake;
    private final Servo SAngle;
    private final Servo SLoaderOut;
    private final ServoImplEx SLoaderUp1, SLoaderUp2;
//    private final LimelightHardware limelight;
    double P = 10;
    double D = 2;
    double F = 0.01030;
    double Kp = 1;
    double[] servoPositions = {0.8492, 0.6389, 0};
    double SLoaderOutHiddenPos = 0.03;
    double SLoaderOutVisiblePos = 0.182;

    boolean isBusy = false;

    int tprShot = 0;
    boolean overwriteShoot;

    public Shooter(HardwareMap hardwareMap) {
        MShooter1 = hardwareMap.get(DcMotorEx.class, "m0");
        MShooter2 = hardwareMap.get(DcMotorEx.class, "m1");
        MShooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setDirection(DcMotorSimple.Direction.REVERSE);

//        MTurnOuttake = hardwareMap.get(DcMotor.class, "m4");

        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, D, F);
        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        SAngle = hardwareMap.get(Servo.class, "s3");
        SAngle.setPosition(0.8492);

        SLoaderUp1 = hardwareMap.get(ServoImplEx.class, "s10");
        SLoaderUp2 = hardwareMap.get(ServoImplEx.class, "s11");

        SLoaderOut = hardwareMap.get(Servo.class, "s9");
        SLoaderOut.setPosition(SLoaderOutHiddenPos);

//        limelight = new LimelightHardware(hardwareMap);
    }

    int FLYWHEEL_VELOCITY_GAIN_DURATION = 500;

    public void shoot(int count, SortBall spindexer, Telemetry telemetry) throws InterruptedException{
//        spindexer.readyToShoot(false, telemetry);
//        sleep(200);

        isBusy = true;
//        double distance = limelight.getAprilTagData().z;
        double distance = 150;
        if(distance <= 95){
            SAngle.setPosition(servoPositions[2]);
            tprShot = (int) (1435.084*Math.pow(distance, 0.06423677));
        } else if (distance <= 200){
            SAngle.setPosition(servoPositions[1]);
            tprShot = (int) (1027.532*Math.pow(distance, 0.1454576));
        } else {
            SAngle.setPosition(servoPositions[0]);
            tprShot = (int) (22.15773*Math.pow(distance, 0.8496951));
        }

//        setMotorVelocity(tprShot, telemetry);
        setMotorVelocity(2200, telemetry);
        sleep(FLYWHEEL_VELOCITY_GAIN_DURATION);

        // load balls
        SLoaderOut.setPosition(SLoaderOutVisiblePos);
        sleep(500);
        SLoaderUp1.setPwmEnable();
        SLoaderUp2.setPwmEnable();

        // START OF CONCURRENT EXECUTION OF SERVO LOADER UP AND SPINDEXER
        Thread servoToggler = new Thread(() -> {
            while (isBusy && !Thread.currentThread().isInterrupted()) {
                SLoaderUp1.setPosition(0.0);
                SLoaderUp2.setPosition(0.0);
                SLoaderUp1.setPosition(0.1);
                SLoaderUp2.setPosition(0.1);
            }
        });
        servoToggler.start();

        spindexer.spinToShooter(count, telemetry);

        servoToggler.interrupt();
        // END OF CONCURRENT EXECUTION
        SLoaderUp1.setPwmDisable();
        SLoaderUp2.setPwmDisable();
        sleep(100);

        SLoaderOut.setPosition(SLoaderOutHiddenPos);
        setMotorVelocity(0, telemetry);

        isBusy = false;
        telemetry.addData("Servo angle", SAngle.getPosition());
        telemetry.addLine("---------------------------");
        telemetry.update();
    }

    public void toggleFlywheel(Telemetry telemetry) {
        int maxShooterVelocity = 2200;
        if(!overwriteShoot) {
            MShooter1.setVelocity(-maxShooterVelocity);
            MShooter2.setVelocity(-maxShooterVelocity);
        } else {
            MShooter1.setVelocity(0);
            MShooter2.setVelocity(0);
        }
        overwriteShoot = !overwriteShoot;
    }

    public void setMotorVelocity(int velocity, Telemetry telemetry){
        MShooter1.setVelocity(-velocity);
        MShooter2.setVelocity(-velocity);

        double curVelocity = MShooter1.getVelocity();
        double error = velocity - curVelocity;

        telemetry.addData("curTargetVelocity", velocity);
        telemetry.addData("curVelocity", curVelocity);
        telemetry.addData("error", error);
        telemetry.addLine("---------------------------");
    }

//    public void trackAprilTag(Telemetry telemetry){
//        double error = limelight.getAprilTagData().x;
//
//        if(error >= 5 || error <= -5){
//            MTurnOuttake.setPower(error*Kp);
//        } else MTurnOuttake.setPower(0);
//
//        telemetry.addData("Tx", error);
//        telemetry.addLine("---------------------------");
//    }

    public boolean isBusy(){
        return isBusy;
    }

    public void updateServoAngle(double angle, Telemetry telemetry){
        double currentAngle = SAngle.getPosition();
        double pos = Math.max(0, Math.min(1, currentAngle + angle));
        SAngle.setPosition(pos);

        telemetry.addData("Pos", pos);
        telemetry.addLine("---------------------------");
    }

    public void updateOuttakeAngle(double rx, Telemetry telemetry){
//        MTurnOuttake.setPower(rx);

        telemetry.addData("Outtake rx", rx);
        telemetry.addLine("---------------------------");
    }

}
