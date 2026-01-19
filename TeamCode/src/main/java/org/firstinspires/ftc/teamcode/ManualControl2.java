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

    public ManualControl2(HardwareMap hardwareMap, Gamepad gamepad) {
//        lifter = new Lifter(hardwareMap);
        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        gamepad2 = gamepad;

        motif = new Motif(hardwareMap);
        spindexer = new SortBall(motif.getSampleMotif(), hardwareMap);
    }

    public void controlTurnOutTake(Telemetry telemetry) {
        double rx = gamepad2.right_stick_x;
        shooter.updateOuttakeAngle(rx, telemetry);
    }

    public void shootBall(Telemetry telemetry) throws InterruptedException{
        if(gamepad2.crossWasPressed() && !shooter.isBusy()){
            shooter.shoot(3, spindexer, telemetry);
        }
    }

    public void controlIntakeShaft(Telemetry telemetry) throws InterruptedException {
        boolean intakeActive = intake.isActive();

        if(gamepad2.triangleWasPressed()) {
            if(intakeActive) intake.stop();
            else intake.start();
        }

        if(intakeActive) {
            spindexer.loadBallsIn(telemetry);
            if (spindexer.isFull()) intake.stop();
        }
    }

    public void updateShooterAngleServo(Telemetry telemetry){
        if(gamepad2.left_trigger > 0.2){
            shooter.updateServoAngle(-gamepad2.left_trigger, telemetry);
        }
        if(gamepad2.right_trigger > 0.2){
            shooter.updateServoAngle(gamepad2.right_trigger, telemetry);
        }
    }

    public void shootPurpleBall(Telemetry telemetry) throws InterruptedException {
        if(!gamepad2.rightBumperWasPressed()) return;

        int purpleIdx = spindexer.getCurrentLoad().indexOf(SortBall.BallColor.PURPLE);
        if (purpleIdx > -1 && !shooter.isBusy()) {
            spindexer.spinTargetToShooter(SortBall.BallColor.PURPLE);
            sleep(200);

            shooter.shoot(1, spindexer, telemetry);
        }
    }

    public void shootGreenBall(Telemetry telemetry) throws InterruptedException {
        if(!gamepad2.leftBumperWasPressed()) return;

        int greenIdx = spindexer.getCurrentLoad().indexOf(SortBall.BallColor.GREEN);
        if (greenIdx > -1 && !shooter.isBusy()) {
            spindexer.spinTargetToShooter(SortBall.BallColor.GREEN);

            shooter.shoot(1, spindexer, telemetry);
        }
    }

    public void toggleFlywheel() {
        if(gamepad2.circleWasPressed()){
            shooter.toggleFlywheel();
        }
    }

}