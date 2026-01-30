package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.Intake;
import org.firstinspires.ftc.teamcode.utils.Lifter;
import org.firstinspires.ftc.teamcode.utils.Shooter;
import org.firstinspires.ftc.teamcode.utils.SortBall;

public class ManualControl2 {
    Shooter shooter;
    Lifter lifter;
    SortBall spindexer;
    Gamepad gamepad2;
    Intake intake;

    int overrideShooterVelocity = 2000;
    Telemetry telemetry;

    public ManualControl2(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry, boolean isRed) {
        shooter = new Shooter(hardwareMap, false, telemetry, isRed);
        intake = new Intake(hardwareMap);
        gamepad2 = gamepad;
        spindexer = new SortBall(hardwareMap, shooter, isRed);
        this.telemetry = telemetry;
    }

    public void shootSingleBall() throws InterruptedException{
        if(gamepad2.squareWasPressed() && !shooter.isBusy()){
            spindexer.goToFirstBall();
            sleep(200);
            shooter.shoot(1, spindexer, 2200);
        }
    }
    public void shootBall() throws InterruptedException{
        if(gamepad2.crossWasPressed() && !shooter.isBusy()){
            spindexer.readyToShoot(false, telemetry);
            sleep(200);
            shooter.shoot(3, spindexer, 2200);
        }
//        if(gamepad2.leftBumperWasPressed() && !shooter.isBusy()){
//            spindexer.readyToShoot(false, telemetry);
//            sleep(200);
//            shooter.LowzoneShoot(3,spindexer,telemetry);
//        }
    }

    public void controlIntakeShaft() throws InterruptedException {
        if(gamepad2.triangleWasPressed()) {
            intake.toggle();
        }

        if(intake.isActive()) {
            spindexer.activateShakeServo();
            spindexer.loadBallsIn(telemetry, gamepad2);
            if (spindexer.isFull()) intake.stop();
        } else {
            spindexer.deactivateShakeServo();
        }
    }

    public void stopThreadShakeServo() throws InterruptedException {
        spindexer.deactivateShakeServo();
    }

    public void updateShooterAngleServo(){
        if(gamepad2.right_bumper){
            shooter.SAngle.setPosition(shooter.SAngle.getPosition()+0.008);
        } else if(gamepad2.left_bumper){
            shooter.SAngle.setPosition(shooter.SAngle.getPosition()-0.008);
        }
        telemetry.addData("Pos", shooter.SAngle.getPosition());
    }

    public void controlTpr() {
        if(gamepad2.rightBumperWasPressed()) {
            overrideShooterVelocity += 100;
        } else if (gamepad2.leftBumperWasPressed()) {
            overrideShooterVelocity -= 100;
        }
        double[] curVelocity = shooter.getVelocity();
        double error = overrideShooterVelocity - curVelocity[0];
        double error2 = overrideShooterVelocity - curVelocity[1];

//        telemetry.addData("target", overrideShooterVelocity);
        telemetry.addData("curVelocity M1", curVelocity[0]);
        telemetry.addData("error M1", error);
        telemetry.addLine("---------------------------");

    }

    public void updateTuneFactor() {
        String next = "p";
        if(gamepad2.squareWasPressed()) {
            next = shooter.goToNextPidf();
        }
        telemetry.addData("Tuning factor", next);
    }
    double[] steps = {0.001, 0.01, 0.1, 1};
    int stepIdx = 0;
    public void updateTuneStep() {
        if(gamepad2.dpadRightWasPressed()) {
            stepIdx = (stepIdx + 1) % steps.length;
        }
    }

    double[] arr = {0, 0, 0, 0};
    public void tuneP() {
        if (gamepad2.right_trigger > 0.2) {
            arr = shooter.tunePidf(steps[stepIdx], telemetry);

        }
        if (gamepad2.left_trigger > 0.2) {
           arr = shooter.tunePidf(-steps[stepIdx], telemetry);
        }
        telemetry.addData("P", arr[0]);
        telemetry.addData("I", arr[1]);
        telemetry.addData("D", arr[2]);
        telemetry.addData("F", arr[3]);
    }

    public void updateIntakeReverse(){
        if(gamepad2.dpad_left){
            intake.reverse();
        }
    }

    public void shootPurpleBall() throws InterruptedException {
        if(!gamepad2.rightBumperWasPressed()) return;

        int purpleIdx = spindexer.getCurrentLoad().indexOf(SortBall.BallColor.PURPLE);
        if (purpleIdx > -1 && !shooter.isBusy()) {
            spindexer.spinTargetToShooter(SortBall.BallColor.PURPLE);
            sleep(200);

            shooter.shoot(1, spindexer, 0);
        }
    }

    public void shootGreenBall() throws InterruptedException {
        if(!gamepad2.leftBumperWasPressed()) return;

        int greenIdx = spindexer.getCurrentLoad().indexOf(SortBall.BallColor.GREEN);
        if (greenIdx > -1 && !shooter.isBusy()) {
            spindexer.spinTargetToShooter(SortBall.BallColor.GREEN);

            shooter.shoot(1, spindexer, 0);
        }
    }

    public void toggleFlywheel() {
        if(gamepad2.circleWasPressed()){
            shooter.toggleFlywheel();
        }
    }

    public void printShooter() {
        shooter.printShooterData();
    }

    public void holdShooter(int goalId) {
        shooter.holdShooter(goalId);
    }

    public void updateMTurnOuttakeAngle() {
        shooter.updateOuttakeAngle();
    }
}