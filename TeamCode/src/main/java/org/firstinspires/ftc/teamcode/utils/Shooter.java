package org.firstinspires.ftc.teamcode.utils;

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
    int WINDOW = 10;
    double[] buffer = new double[WINDOW];
    int index = 0;
    int count = 0;


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

    int getAverage(double newValue) {
        buffer[index] = newValue;
        index = (index + 1) % WINDOW;
        count = Math.min(count + 1, WINDOW);

        double sum = 0;
        for (int i = 0; i < count; i++) sum += buffer[i];

        return (int) sum / count;
    }

    public void shoot(Telemetry telemetry) throws InterruptedException{
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
        wait(1000);

        // load balls
        SLoader.setPosition(0.5);
        wait(1000);

        // reset shooter
        MLoader.setPower(0);
        SLoader.setPosition(0);
        MShooter1.setPower(0);
        MShooter2.setPower(0);
        wait(1000);

        isBusy = false;
        telemetry.addData("Servo angle", SAngle.getPosition());
        telemetry.addLine("---------------------------");
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
        double distance = limelight.getAprilTagData().z;

        if(error >= 5 || error <= -5){
            MTurnOuttake.setPower(distance*Kp);
        } else MTurnOuttake.setPower(0);

        telemetry.addData("Distance", distance);
        telemetry.addData("Tx", error);
        telemetry.addLine("---------------------------");
    }

    public boolean isBusy(){
        return isBusy;
    }
}
