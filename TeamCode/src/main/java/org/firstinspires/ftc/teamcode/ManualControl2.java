package org.firstinspires.ftc.teamcode;

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

    public ManualControl2(HardwareMap hardwareMap) {
        lifter = new Lifter(hardwareMap);
        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);

        samples = new ArrayList<>();
        samples.add(SortBall.BallColor.PURPLE);
        samples.add(SortBall.BallColor.PURPLE);
        samples.add(SortBall.BallColor.GREEN);

        spindexer = new SortBall(samples, hardwareMap);
    }

    public void controlTurnOutTake() {
        double rx = Gamepad2.right_stick_x;
        shooter.updateOuttakeAngle(rx);
    }

    public void shootBall(Telemetry telemetry) throws InterruptedException{
        if(Gamepad2.crossWasPressed()){
            shooter.shoot(3, spindexer, telemetry);
        }
    }

    public void controlIntakeShaft() throws InterruptedException {
        boolean intakeActive = intake.isActive();

        if(Gamepad2.triangleWasPressed()) {
            if(intakeActive) intake.stop();
            else intake.start();
        }

        if(intakeActive) spindexer.loadBallsIn();
    }

    public void updateShooterAngleServo(){
        if(Gamepad2.left_trigger > 0.2){
            shooter.updateServoAngle(-Gamepad2.left_trigger);
        }
        if(Gamepad2.right_trigger > 0.2){
            shooter.updateServoAngle(Gamepad2.right_trigger);
        }
    }

    public void shootPurpleBall(Telemetry telemetry) throws InterruptedException {
        if(!Gamepad2.leftBumperWasPressed()) return;

        int purpleIdx = spindexer.getCurrentLoad().indexOf(SortBall.BallColor.PURPLE);
        if (purpleIdx > -1
                && !shooter.isBusy()) {
            spindexer.rotateToBall(SortBall.BallColor.PURPLE);
            shooter.shoot(1, spindexer, telemetry);
        }
    }

    public void shootGreenBall(Telemetry telemetry) throws InterruptedException {
        if(!Gamepad2.leftBumperWasPressed()) return;

        int purpleIdx = spindexer.getCurrentLoad().indexOf(SortBall.BallColor.GREEN);
        if (purpleIdx > -1
                && !shooter.isBusy()) {
            spindexer.rotateToBall(SortBall.BallColor.GREEN);
            shooter.shoot(1, spindexer, telemetry);
        }
    }

    public void toggleFlywheel() {
        if(Gamepad2.circleWasPressed()){
            shooter.toggleFlywheel();
        }
    }
}