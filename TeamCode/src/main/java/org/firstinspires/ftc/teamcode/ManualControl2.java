package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.Intake;
import org.firstinspires.ftc.teamcode.utils.Lifter;
import org.firstinspires.ftc.teamcode.utils.Motif;
import org.firstinspires.ftc.teamcode.utils.Shooter;
import org.firstinspires.ftc.teamcode.utils.SortBall;

public class ManualControl2 {
    Shooter shooter;
    Lifter lifter;
    SortBall spindexer;
    Gamepad gamepad2;
    Intake intake;
    Motif motif;
    boolean readyToShot = false;

    int overrideShooterVelocity = 3000;

    public ManualControl2(HardwareMap hardwareMap, Gamepad gamepad) {
//        lifter = new Lifter(hardwareMap);
        shooter = new Shooter(hardwareMap, true);
        intake = new Intake(hardwareMap);
        gamepad2 = gamepad;

        motif = new Motif(hardwareMap);
        spindexer = new SortBall(hardwareMap, shooter);
    }

    public void controlTurnOutTake(Telemetry telemetry) {
        if(gamepad2.dpad_left) {
            shooter.updateOuttakeAngle(-0.5);
        } else if(gamepad2.dpad_right) {
            shooter.updateOuttakeAngle(0.5);
        } else shooter.updateOuttakeAngle(0);

    }

    public void shootBall(Telemetry telemetry) throws InterruptedException{
        if(gamepad2.crossWasPressed() && !shooter.isBusy()){
            spindexer.readyToShoot(false, telemetry);
            sleep(200);
            shooter.shoot(3, spindexer, telemetry, overrideShooterVelocity);
        }
    }

    public void controlIntakeShaft(Telemetry telemetry) throws InterruptedException {
        boolean intakeActive = intake.isActive();

        if(gamepad2.triangleWasPressed()) {
            if(intakeActive) intake.stop();
            else intake.start();
        }

        if(intakeActive) {
            spindexer.loadBallsIn(telemetry, gamepad2);
            if (spindexer.isFull()) intake.stop();
        }
    }
    public void updateReadyToShot(Telemetry telemetry){
        if(gamepad2.squareWasPressed()){
            if(!readyToShot) {
                spindexer.readyToShoot(false, telemetry);
                readyToShot = true;
            } else {
                shooter.setMotorVelocity(0, telemetry);
                readyToShot = false;
            }
        }
    }
    public void updateShooterAngleServo(Telemetry telemetry){
        if(gamepad2.dpad_up){
            shooter.SAngle.setPosition(shooter.SAngle.getPosition()+0.0008);
        } else if(gamepad2.dpad_down){
            shooter.SAngle.setPosition(shooter.SAngle.getPosition()-0.0008);
        }
        telemetry.addData("Pos", shooter.SAngle.getPosition());
    }

    public void controlTpr(Telemetry telemetry) {
        if(gamepad2.rightBumperWasPressed()) {
            overrideShooterVelocity += 100;
        } else if (gamepad2.leftBumperWasPressed()) {
            overrideShooterVelocity -= 100;
        }
        telemetry.addData("overrideShooterVelocity", overrideShooterVelocity);
        telemetry.update();
    }

    public void updateIntakeReverse(){
        if(gamepad2.dpad_left){
            intake.reverse();
        } else if(gamepad2.dpad_right){
            intake.stop();
        }
    }

    public void shootPurpleBall(Telemetry telemetry) throws InterruptedException {
        if(!gamepad2.rightBumperWasPressed()) return;

        int purpleIdx = spindexer.getCurrentLoad().indexOf(SortBall.BallColor.PURPLE);
        if (purpleIdx > -1 && !shooter.isBusy()) {
            spindexer.spinTargetToShooter(SortBall.BallColor.PURPLE);
            sleep(200);

            shooter.shoot(1, spindexer, telemetry, 0);
        }
    }

    public void shootGreenBall(Telemetry telemetry) throws InterruptedException {
        if(!gamepad2.leftBumperWasPressed()) return;

        int greenIdx = spindexer.getCurrentLoad().indexOf(SortBall.BallColor.GREEN);
        if (greenIdx > -1 && !shooter.isBusy()) {
            spindexer.spinTargetToShooter(SortBall.BallColor.GREEN);

            shooter.shoot(1, spindexer, telemetry, 0);
        }
    }

    public void toggleFlywheel(Telemetry telemetry) {
        if(gamepad2.circleWasPressed()){
            shooter.toggleFlywheel(telemetry);
        }
    }

    public void holdShooter(int goalId, Telemetry telemetry) {
        shooter.HoldShooter(goalId, telemetry, true);
    }
}