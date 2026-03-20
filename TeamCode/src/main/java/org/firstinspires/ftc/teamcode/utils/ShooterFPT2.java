package org.firstinspires.ftc.teamcode.utils;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ShooterFPT2 {
    private LimelightHardware limelight;
    public final DcMotorEx MShooter1, MShooter2;
    public final DcMotorEx MTurnOuttake;
    public Servo SAngle;
    public RevBlinkinLedDriver led;
    public double P = 22, I = 0.75, D = 0.25, F = 1.6;
    public int tprShot = 1;
    int maxTick = 670, minTick = -670;
    private IMU imu;
    public double lastError, kp = 0.005, ki = 0.00025,  kd=0.0025;
    double goalX = 0, angleTolerance = 0, power = 0;

    ElapsedTime timer = new ElapsedTime();

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

        led = hardwareMap.get(RevBlinkinLedDriver.class, "s9");

        SAngle = hardwareMap.get(Servo.class, "s3");
        limelight = new LimelightHardware(hardwareMap);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        );
        imu.resetYaw();
        timer.reset();
    }

    @SuppressLint("SuspiciousIndentation")
    public void shoot(Telemetry telemetry) throws InterruptedException {
        double distance = 100;
        if (limelight.getAprilTagData(telemetry) != null)
            distance = limelight.getAprilTagData(telemetry).z;
            telemetry.addData("distance", distance);
            if (distance <= 115) {
                tprShot = (int) (1400 + (distance-115)*0.1);
                SAngle.setPosition(calculateAngle(distance, 0.025));
            } else if (distance <= 240) {
                SAngle.setPosition(calculateAngle(distance, 0.025));
                tprShot = (int) (1500 + (distance-115)*0.1);
            } else {
                tprShot = 2800;
                SAngle.setPosition(calculateAngle(distance,-0.05));
            }
            telemetry.addData("error", tprShot-MShooter1.getVelocity());
            setMotorVelocity(tprShot);
            if(tprShot-MShooter1.getVelocity() < 30){
                led.setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            } else {
                led.setPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE);
            }
        }
        public void setMotorVelocity(int velocity){
            MShooter1.setVelocity(velocity);
            MShooter2.setVelocity(velocity);
        }

        public double calculateAngle(double dis, double offset){
            double a = -1.5015e-05;
            double b =  0.0064733;
            double c = -0.0007912;
//            double offset = 0.05;

            double pos = (a * dis * dis + b * dis + c) - offset;
            return Math.max(0.0, Math.min(1.0, pos));
        }
    public void holdShooter(int id, Telemetry telemetry, boolean reverseMotor) {
        if (reverseMotor) MTurnOuttake.setDirection(DcMotorSimple.Direction.REVERSE);
        else MTurnOuttake.setDirection(DcMotorSimple.Direction.FORWARD);
        ApriltagData data = limelight.getAprilTagData(telemetry);
        if(data == null || MTurnOuttake.getCurrentPosition()>maxTick || MTurnOuttake.getCurrentPosition()<-maxTick){
            MTurnOuttake.setPower(0);
            lastError = 0;
            return;
        }
        if(data.id != id){
            MTurnOuttake.setPower(0);
            lastError = 0;
            return;
        }
        double deltaTime = timer.seconds();
        timer.reset();
        double error = goalX - data.x;
        double pTerm = error*kp; // Kp
        double dTerm = 0;
        if(deltaTime > 0){
            dTerm = ((error - lastError)/deltaTime)*kd;
        }

        if(Math.abs(error) < angleTolerance){
            power = 0;
        } else {
            power = Range.clip(pTerm + dTerm, -0.35, 0.35);
        }
        MTurnOuttake.setPower(power);
        lastError = error;
        telemetry.addData("encoder", MTurnOuttake.getCurrentPosition());
    }
    public void holdMotor(DcMotor motor) {
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.75);
    }
}
