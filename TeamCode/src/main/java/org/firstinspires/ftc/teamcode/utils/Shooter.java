package org.firstinspires.ftc.teamcode.utils;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Shooter {
    private final DcMotorEx MShooter1, MShooter2, MLoader;
    private final DcMotor MTurnOuttake;
    private final Servo SAngle, SLoader;
    private final LimelightHardware limelight;
    public ElapsedTime timer = new ElapsedTime();
    double P = 15.1;
    double F = 0.0112;
    double Kp = 1;
    double[] servoPositions = {0.8492, 0.6389, 0};
    boolean isBusy = false;

    int tprShot = 0;
    boolean overwriteShoot;

    public Shooter(HardwareMap hardwareMap) {
        MShooter1 = hardwareMap.get(DcMotorEx.class, "m3");
        MShooter2 = hardwareMap.get(DcMotorEx.class, "m4");
        MShooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        MLoader = hardwareMap.get(DcMotorEx.class, "m5");
        MTurnOuttake = hardwareMap.get(DcMotor.class, "m6");

        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);
        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        MLoader.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        SAngle = hardwareMap.get(Servo.class, "s1");
        SAngle.setPosition(0.8492);
        
        SLoader = hardwareMap.get(Servo.class, "s2");
        SLoader.setPosition(0.5);

        limelight = new LimelightHardware(hardwareMap);
        timer.reset();
    }

    int FLYWHEEL_VELOCITY_GAIN_DURATION = 1000;

    public void shoot(int count, SortBall spindexer, Telemetry telemetry) throws InterruptedException{
        isBusy = true;

        double distance = limelight.getAprilTagData().z;
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

        setMotorVelocity(tprShot, telemetry);
        sleep(FLYWHEEL_VELOCITY_GAIN_DURATION);

        // load balls
        SLoader.setPosition(0.5);
        sleep(500);
        MLoader.setPower(1);

        spindexer.rotateToShooter(count);

        // reset shooter
        MLoader.setPower(0);
        SLoader.setPosition(0);
        MShooter1.setVelocity(0);
        MShooter2.setVelocity(0);
        sleep(1000);

        isBusy = false;
        telemetry.addData("Servo angle", SAngle.getPosition());
        telemetry.addLine("---------------------------");
    }

    public void toggleFlywheel() {
        if(!overwriteShoot) {
            int maxShooterVelocity = 2200;
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
        MLoader.setVelocity(300);

        double curVelocity = MShooter1.getVelocity();
        double error = velocity - curVelocity;

        telemetry.addData("curTargetVelocity", velocity);
        telemetry.addData("curVelocity", curVelocity);
        telemetry.addData("error", error);
        telemetry.addLine("---------------------------");
    }

    public void trackAprilTag(Telemetry telemetry){
        double error = limelight.getAprilTagData().x;

        if(error >= 5 || error <= -5){
            MTurnOuttake.setPower(error*Kp);
        } else MTurnOuttake.setPower(0);

        telemetry.addData("Tx", error);
        telemetry.addLine("---------------------------");
    }

    public boolean isBusy(){
        return isBusy;
    }

    public void updateServoAngle(double angle){
        double currentAngle = SAngle.getPosition();
        SAngle.setPosition(currentAngle + angle);
    }

    public void updateOuttakeAngle(double rx){
        MTurnOuttake.setPower(rx);
    }

}
