package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.Intake;
import org.firstinspires.ftc.teamcode.utils.Lifter;
import org.firstinspires.ftc.teamcode.utils.Shooter;
import org.firstinspires.ftc.teamcode.utils.SortBall;

import java.util.ArrayList;
import java.util.List;

public class ManualControl2 {
    Shooter shooter;
    Lifter lifter;
    SortBall spindexer;
    Gamepad Gamepad2;
    Intake intake;
    List<SortBall.BallColor> samples;

    public ManualControl2(HardwareMap hardwareMap, Gamepad gamepad) {
//        lifter = new Lifter(hardwareMap);
        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        Gamepad2 = gamepad;

        samples = new ArrayList<>();
        samples.add(SortBall.BallColor.PURPLE);
        samples.add(SortBall.BallColor.PURPLE);
        samples.add(SortBall.BallColor.GREEN);

        spindexer = new SortBall(samples, hardwareMap);
    }

    public void controlTurnOutTake(Telemetry telemetry) {
        double rx = Gamepad2.right_stick_x;
        shooter.updateOuttakeAngle(rx, telemetry);
    }

    public void shootBall(Telemetry telemetry) throws InterruptedException{
        if(Gamepad2.crossWasPressed() && !shooter.isBusy()){
            shooter.shoot(3, spindexer, telemetry);
        }
    }

    public void controlIntakeShaft(Telemetry telemetry) throws InterruptedException {
        boolean intakeActive = intake.isActive();

        if(Gamepad2.triangleWasPressed()) {
            if(intakeActive) intake.stop();
            else intake.start();
        }

        if(intakeActive) spindexer.loadBallsIn(telemetry);
    }

    public void updateShooterAngleServo(Telemetry telemetry){
        if(Gamepad2.left_trigger > 0.2){
            shooter.updateServoAngle(-Gamepad2.left_trigger, telemetry);
        }
        if(Gamepad2.right_trigger > 0.2){
            shooter.updateServoAngle(Gamepad2.right_trigger, telemetry);
        }
    }

    public void shootPurpleBall(Telemetry telemetry) throws InterruptedException {
        if(!Gamepad2.rightBumperWasPressed()) return;

        int purpleIdx = spindexer.getCurrentLoad().indexOf(SortBall.BallColor.PURPLE);
        if (purpleIdx > -1 && !shooter.isBusy()) {
            spindexer.spinTargetToShooter(SortBall.BallColor.PURPLE);
            sleep(200);

            shooter.shoot(1, spindexer, telemetry);
        }
    }

    public void shootGreenBall(Telemetry telemetry) throws InterruptedException {
        if(!Gamepad2.leftBumperWasPressed()) return;

        int greenIdx = spindexer.getCurrentLoad().indexOf(SortBall.BallColor.GREEN);
        if (greenIdx > -1 && !shooter.isBusy()) {
            spindexer.spinTargetToShooter(SortBall.BallColor.GREEN);

            shooter.shoot(1, spindexer, telemetry);
        }
    }

    public void toggleFlywheel() {
        if(Gamepad2.circleWasPressed()){
            shooter.toggleFlywheel();
        }
    }

    public void readyToShoot() {
        boolean empty = spindexer.getCurrentLoad().isEmpty();
        if(Gamepad2.squareWasPressed() && !empty && !shooter.isBusy()) {
            spindexer.readyToShoot();
        }
    }
}