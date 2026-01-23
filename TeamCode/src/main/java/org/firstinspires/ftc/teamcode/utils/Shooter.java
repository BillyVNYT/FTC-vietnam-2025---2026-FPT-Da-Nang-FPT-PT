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
    //---coefficient PID---
    // ===== PID for Outtake Turning =====
    private double turnKp = 0.04;
    private double turnKi = 0.0;
    private double turnKd = 0.0;

    private double turnIntegral = 0;
    private double lastTxError = 0;
    private long lastTurnTime = 0;

    //---coeefficient PID---

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

    double[] servoPositions = {0.8492, 0.6389, 0};

    double SLoaderOutHiddenPos = 0.03;
    double SLoaderOutVisiblePos = 0.182;

    volatile boolean isBusy = false;

    int tprShot = 1;
    boolean overwriteShoot;

    // ===== Overcurrent protection =====
    private static final double CURRENT_LIMIT = 10.0;   // Amp
    private static final long OVERCURRENT_TIME = 150;   // ms
    private static final long REVERSE_COOLDOWN = 300;     // ms
    private static final long REVERSE_TIME = 300;       // ms

    private long overCurrentStartTime = 0;
    private long reverseStartTime = 0;
    private boolean isReversing = false;

    // ================= CONSTRUCTOR =================

    public Shooter(HardwareMap hardwareMap) {

        MShooter1 = hardwareMap.get(DcMotorEx.class, "m0");
        MShooter2 = hardwareMap.get(DcMotorEx.class, "m1");

        MShooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        MTurnOuttake = hardwareMap.get(DcMotorEx.class, "m4");
        MTurnOuttake.setDirection(DcMotorSimple.Direction.FORWARD);

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
    }
    private double getTurnPowerPID(double tx){

        double error = tx;   // target = 0

        long now = System.currentTimeMillis();
        double dt = (now - lastTurnTime) / 1000.0;

        if (dt <= 0 || dt > 0.1) dt = 0.02; // chống dt bậy

        turnIntegral += error * dt;
        double derivative = (error - lastTxError) / dt;

        double output = turnKp * error + turnKi * turnIntegral + turnKd * derivative;

        lastTxError = error;
        lastTurnTime = now;

        // limit power
        output = Math.max(-0.5, Math.min(0.5, output));

        return output;
    }



    int FLYWHEEL_VELOCITY_GAIN_DURATION = 500;

    // ================= SHOOT =================

    public void shoot(int count, SortBall spindexer, Telemetry telemetry) throws InterruptedException {

        isBusy = true;

        double distance = limelight.getAprilTagData(telemetry).z;

        if(distance <= 165){
            SAngle.setPosition(calculateAngle(distance, spindexer.is_lastBall, telemetry));
            tprShot = 2000;
        } else if (distance <= 240){
            SAngle.setPosition(calculateAngle(distance, spindexer.is_lastBall, telemetry));
            tprShot = 2600;
        } else {
            SAngle.setPosition(calculateAngle(distance, spindexer.is_lastBall, telemetry));
            tprShot = 3000;
        }

        setMotorVelocity(tprShot, telemetry);
        sleep(FLYWHEEL_VELOCITY_GAIN_DURATION);

        // load balls
        SLoaderOut.setPosition(SLoaderOutVisiblePos);
        sleep(500);

        SLoaderUp1.setPwmEnable();
        SLoaderUp2.setPwmEnable();

        // ===== SERVO TOGGLER THREAD =====
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
            } catch (InterruptedException ignored) {}
        });
        servoToggler.start();

        spindexer.spinToShooter(count, telemetry);

        servoToggler.interrupt();

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

    // ================= FLYWHEEL =================

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

    // ================= MANUAL TURN =================

    public void updateOuttakeAngle(double rx, Telemetry telemetry){
        MTurnOuttake.setPower(rx);
    }

    // ================= HOLD SHOOTER (AIM + JAM PROTECT) =================

    public void HoldShooter(int id, Telemetry telemetry) {

        limelight.changePipeline(0);
        ApriltagData data = limelight.getAprilTagData(telemetry);

        if (data == null || data.id != id) {
            MTurnOuttake.setPower(0);
            return;
        }

        // ================= ALIGN BY TX =================
        double Tx = data.x;
        double power = getTurnPowerPID(Tx);

        // deadzone nhỏ để khỏi rung
        if (Math.abs(Tx) < 0.5) {
            power = 0;
        }


        // ================= CURRENT CHECK =================
        double current = MTurnOuttake.getCurrent(CurrentUnit.AMPS);
        long now = System.currentTimeMillis();

        if (current > CURRENT_LIMIT) {
            if (overCurrentStartTime == 0)
                overCurrentStartTime = now;

            if (now - overCurrentStartTime > OVERCURRENT_TIME && !isReversing) {
                isReversing = true;
                reverseStartTime = now;
            }
        } else {
            overCurrentStartTime = 0;
        }

        // ================= REVERSE LOGIC =================
        if (isReversing) {
            MTurnOuttake.setPower(-0.3);

            if (now - reverseStartTime > REVERSE_COOLDOWN) {
                isReversing = false;
            }
        } else {
            MTurnOuttake.setPower(power);
        }

        // ================= TELEMETRY =================
        telemetry.addData("Tx", Tx);
        telemetry.addData("Turn Power", MTurnOuttake.getPower());
        telemetry.addData("Current (A)", current);
        telemetry.addData("Reversing", isReversing);
        telemetry.update();
    }


    // ================= ANGLE =================

    public double calculateAngle(double dis, boolean is_lastBall, Telemetry telemetry){

        double a = -1.5015e-05;
        double b =  0.0064733;
        double c = -0.0007912;

        double offset = 0.275;

        if(is_lastBall){
            telemetry.addLine("IS LAST BALL");
        } else {
            telemetry.addLine("NOT LAST BALL");
        }
        telemetry.update();

        double pos = a * dis * dis + b * dis + c - offset;
        return Math.max(0.0, Math.min(1.0, pos));
    }

    public void updateServoAngle(double degree, Telemetry telemetry){
        SAngle.setPosition(0.03638079 * degree - 0.8736323);
    }
}
