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

    double P = 6;
    double I = 0;
    double D = 0;
    double F = 0.0085;
    double[][] hoodTable = {
            {150, 0.6256},
            {180, 0.7294},
            {210, 0.5389},
            {220, 0.7339},
            {230, 0.7906},
            {265, 0.6389}
    };
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
        MShooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        MTurnOuttake = hardwareMap.get(DcMotorEx.class, "m4");
        MTurnOuttake.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        SAngle = hardwareMap.get(Servo.class, "s3");
        SAngle.setDirection(Servo.Direction.REVERSE);
        SAngle.setPosition(0.8492);

        SLoaderUp1 = hardwareMap.get(ServoImplEx.class, "s10");
        SLoaderUp2 = hardwareMap.get(ServoImplEx.class, "s11");

        SLoaderOut = hardwareMap.get(Servo.class, "s9");
        SLoaderOut.setPosition(SLoaderOutHiddenPos);

        limelight = new LimelightHardware(hardwareMap);
        limelight.changePipeline(0);
    }

    int FLYWHEEL_VELOCITY_GAIN_DURATION = 500;

    public void shoot(int count, SortBall spindexer, Telemetry telemetry) throws InterruptedException{
        isBusy = true;
        double distance = limelight.getAprilTagData(telemetry).z;
        SAngle.setPosition(calculateAngle(distance, spindexer.is_lastBall, telemetry));

        if(distance <= 165){
            tprShot = 2000;
        } else if (distance <= 240){
            tprShot = 2600;
        } else {
            tprShot = 3000;
        }

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
    public boolean HoldShooter(int id, Telemetry telemetry, boolean reverseMotor){
        if(reverseMotor) MTurnOuttake.setDirection(DcMotorSimple.Direction.REVERSE);
        else MTurnOuttake.setDirection(DcMotorSimple.Direction.FORWARD);

        ApriltagData data = limelight.getAprilTagData(telemetry);
        boolean locked = false;

        if(data != null && data.id == id){
            double Tx = limelight.getAprilTagData(telemetry).x;
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
                locked = true;
            }

            double current = MTurnOuttake.getCurrent(CurrentUnit.AMPS);
            if (current > 7) {
                MTurnOuttakeReverse = true;
            }
            double curVelocity = MShooter1.getVelocity();
            double error = 2600 - curVelocity;

            telemetry.addData("curTargetVelocity", 2600);
            telemetry.addData("curVelocity", curVelocity);
            telemetry.addData("error", error);
            telemetry.addLine("---------------------------");
            telemetry.addData("Tx", Tx);
            telemetry.addData("distance", data.z);
            telemetry.update();

            return locked;
        } else {
            MTurnOuttake.setPower(0);
            return false;
        }
    }
    public double calculateAngle(double dis, boolean is_lastBall, Telemetry telemetry){
        double a = -1.5015e-05;
        double b =  0.0064733;
        double c = -0.0007912;

        double offset = 0.275; // chỉnh cao lên
        if(is_lastBall){}

        double pos = a * dis * dis + b * dis + c - offset;
        return Math.max(0.0, Math.min(1.0, pos));
    }

    public void updateServoAngle(double degree, Telemetry telemetry){
        SAngle.setPosition(0.03638079*degree - 0.8736323);
    }
}
