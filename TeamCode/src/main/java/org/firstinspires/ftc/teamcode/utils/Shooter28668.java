package org.firstinspires.ftc.teamcode.utils;

import static java.lang.Thread.sleep;

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

public class Shooter28668 {
    private LimelightHardware limelight;
    public final DcMotorEx MShooter1, MShooter2;
    private final DcMotorEx MTurnOuttake;
    public Servo SAngle, SGate1, SGate2, SBackKick;
    double SAngleLowest = 0.8492, SBackKickOff = 0, SBackKickOn = 0.9;
    double SGate1Close = 0.03, SGate1Open = 0.182, SGate2Close = 0.03, SGate2Open = 0.182;
    double P = 6, I = 0, D = 0, F = 0.0085;
    boolean MTurnOuttakeReverse = false;
    volatile boolean isBusy = false;
    int tprShot = 1;
    boolean overwriteShoot;
    int FLYWHEEL_VELOCITY_GAIN_DURATION = 200;
    int THREE_BALLS_SHOOTING_DURATION = 1000;
    int MIN_TPR = 2000;
    private final DistanceSensor DSensor;
    private final Intake intake;
    boolean isFull = false;
    ElapsedTime timer = new ElapsedTime();

    public Shooter28668(HardwareMap hardwareMap, Intake intake) {
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
        SAngle.setPosition(SAngleLowest);

        SGate1 = hardwareMap.get(Servo.class, "s10");
        SGate2 = hardwareMap.get(Servo.class, "s11");
        SBackKick = hardwareMap.get(Servo.class, "s9");
        resetShooterServos();

        limelight = new LimelightHardware(hardwareMap);
        DSensor = hardwareMap.get(DistanceSensor.class, "ds");
        this.intake = intake;
    }

    public void checkTunnelFull() {
        if(isBusy || !intake.isActive()) return;

        double distance = DSensor.getDistance(DistanceUnit.CM);

        if (distance < 5) {
            if(timer.seconds() > 2 && !isFull) {
                isFull = true;
                intake.stop();
                setMotorVelocity(MIN_TPR);
            }
        } else if (distance > 5) {
            isFull = false;
            timer.reset();
        }
    }

    public void shoot(Telemetry telemetry) throws InterruptedException {
        isBusy = true;
        double distance = limelight.getAprilTagData(telemetry).z;
        SAngle.setPosition(calculateAngle(distance));

        if(distance <= 165){
            tprShot = MIN_TPR;
        } else if (distance <= 240){
            tprShot = 2600;
        } else {
            tprShot = 3000;
        }

        setMotorVelocity(tprShot);
        sleep(FLYWHEEL_VELOCITY_GAIN_DURATION);

        intake.start();
        SBackKick.setPosition(SBackKickOn);
        SGate1.setPosition(SGate1Open);
        SGate2.setPosition(SGate2Open);

        sleep(THREE_BALLS_SHOOTING_DURATION);

        resetShooterServos();
        setMotorVelocity(0);
        intake.stop();

        isBusy = false;
    }

    private void resetShooterServos() {
        SBackKick.setPosition(SBackKickOff);
        SGate1.setPosition(SGate1Close);
        SGate2.setPosition(SGate2Close);
    }

    public void toggleFlywheel() {
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

    public void setMotorVelocity(int velocity){
        MShooter1.setVelocity(velocity);
        MShooter2.setVelocity(velocity);
    }

    public boolean isBusy(){
        return isBusy;
    }

    public double calculateAngle(double dis){
        double a = -1.5015e-05;
        double b =  0.0064733;
        double c = -0.0007912;

        double pos = a * dis * dis + b * dis + c;
        return Math.max(0.0, Math.min(1.0, pos));
    }
    public void holdShooter(int id, Telemetry telemetry, boolean reverseMotor){
        if(reverseMotor) MTurnOuttake.setDirection(DcMotorSimple.Direction.REVERSE);
        else MTurnOuttake.setDirection(DcMotorSimple.Direction.FORWARD);
        ApriltagData data = limelight.getAprilTagData(telemetry);

        if(data == null || data.id != id){
            MTurnOuttake.setPower(0);
            return;
        }

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
        }

        double current = MTurnOuttake.getCurrent(CurrentUnit.AMPS);
        if (current > 7) {
            MTurnOuttakeReverse = true;
        }

        telemetry.addLine("---------------------------");
        telemetry.addData("Tx", Tx);
        telemetry.addData("distance", data.z);
    }
}
