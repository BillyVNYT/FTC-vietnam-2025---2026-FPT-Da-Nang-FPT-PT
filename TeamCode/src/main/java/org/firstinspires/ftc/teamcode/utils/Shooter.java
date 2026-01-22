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
    private LimelightHardware limelight;
    public final DcMotorEx MShooter1, MShooter2;
    private final DcMotorEx MTurnOuttake;
    public Servo SAngle;
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
    boolean MTurnOuttakeReverse = false;

    volatile boolean isBusy = false;

    int tprShot = 1;
    boolean overwriteShoot;

    public Shooter(HardwareMap hardwareMap) {
        MShooter1 = hardwareMap.get(DcMotorEx.class, "m0");
        MShooter2 = hardwareMap.get(DcMotorEx.class, "m1");
        MShooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        MTurnOuttake = hardwareMap.get(DcMotorEx.class, "m4");
        MTurnOuttake.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, D, F);
        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        SAngle = hardwareMap.get(Servo.class, "s3");
        SAngle.setDirection(Servo.Direction.REVERSE);
        SAngle.setPosition(0.8492);

        SLoaderUp1 = hardwareMap.get(ServoImplEx.class, "s10");
        SLoaderUp2 = hardwareMap.get(ServoImplEx.class, "s11");

        SLoaderOut = hardwareMap.get(Servo.class, "s9");
        SLoaderOut.setPosition(SLoaderOutHiddenPos);

        limelight = new LimelightHardware(hardwareMap);
    }

    int FLYWHEEL_VELOCITY_GAIN_DURATION = 500;

    public void shoot(int count, SortBall spindexer, Telemetry telemetry) throws InterruptedException{
        isBusy = true;
        double distance = limelight.getAprilTagData().z;
//        double distance = 150;
        if(distance <= 140){
            SAngle.setPosition(calculateAngle(distance));
            tprShot = 2100;
        } else if (distance <= 240){
            SAngle.setPosition(calculateAngle(distance));
            tprShot = 2600;
        } else {
            SAngle.setPosition(calculateAngle(distance));
            tprShot = 3100;
        }

//        setMotorVelocity(tprShot, telemetry);
        setMotorVelocity(tprShot, telemetry);
        sleep(FLYWHEEL_VELOCITY_GAIN_DURATION);

        // load balls
        SLoaderOut.setPosition(SLoaderOutVisiblePos);
        sleep(500);
        SLoaderUp1.setPwmEnable();
        SLoaderUp2.setPwmEnable();

        // START OF CONCURRENT EXECUTION OF SERVO LOADER UP AND SPINDEXER
        Thread servoToggler = new Thread(() -> {
            try {
                while (isBusy) {
                    SLoaderUp1.setPosition(0.0);
                    SLoaderUp2.setPosition(0.0);
                    Thread.sleep(120);

                    SLoaderUp1.setPosition(0.1);
                    SLoaderUp2.setPosition(0.1);
                    Thread.sleep(120);
                }
            } catch (InterruptedException e) {
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

    public boolean isBusy(){
        return isBusy;
    }

    public void updateOuttakeAngle(double rx, Telemetry telemetry){
        MTurnOuttake.setPower(rx);
    }
    public void HoldShooter(int id, Telemetry telemetry){
        limelight.changePipeline(0);
        ApriltagData data = limelight.getAprilTagData();
        if(data.id == id) {
            telemetry.addData("distance", data.z);
            double Tx = limelight.getAprilTagData().x;
            if (Math.abs(Tx) > 1) {
                double power = Tx*0.04;
                if(power > 0.5) { power = 0.5; };
                if(!MTurnOuttakeReverse) {
                    MTurnOuttake.setPower(power);
                } else {
                    MTurnOuttake.setPower(-power);
                }
            } else {
                MTurnOuttake.setPower(0);
                MTurnOuttakeReverse = false;
            }
            double current = MTurnOuttake.getCurrent(CurrentUnit.AMPS);

            if (current > 7) {
                MTurnOuttakeReverse = true;
            }
            telemetry.addData("Tx", Tx);
            telemetry.update();
        }
    }
    public double calculateAngle(double dis){
        return 0;
    }
    public void updateServoAngle(double degree){
        SAngle.setPosition(0.03638079*degree - 0.8736323);
    }
}
