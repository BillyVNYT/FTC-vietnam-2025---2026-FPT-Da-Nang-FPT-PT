package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.Intake;
import org.firstinspires.ftc.teamcode.utils.Lifter;
import org.firstinspires.ftc.teamcode.utils.Motif;
import org.firstinspires.ftc.teamcode.utils.ShooterFPT2;

public class ManualControl {

    ShooterFPT2 shooter;
    Lifter lifter;
    Gamepad gamepad2;
    Intake intake;
    Motif motif;

    public ManualControl(HardwareMap hardwareMap, Gamepad gamepad) {
//        lifter = new Lifter(hardwareMap);
        shooter = new ShooterFPT2(hardwareMap, intake);
        intake = new Intake(hardwareMap);
        gamepad2 = gamepad;
        motif = new Motif(hardwareMap);
    }

    public void shootBall(Telemetry telemetry) throws InterruptedException{
        if(gamepad2.crossWasPressed() && !shooter.isBusy()){
            shooter.shoot(telemetry);
        }
        if(gamepad2.triangleWasPressed()&&!shooter.isBusy()){
            shooter.shootAtLowZone(telemetry);
        }
    }

    public void toggleIntake(){
        boolean intakeActive = intake.isActive();
        if(gamepad2.triangleWasPressed() && !shooter.isBusy()) {
            if(intakeActive) intake.stop();
            else intake.start();
        }
    }

    public void updateShooterAngleServo(Telemetry telemetry){
        if(gamepad2.dpad_up){
            shooter.SAngle.setPosition(shooter.SAngle.getPosition()+0.0008);
        } else if(gamepad2.dpad_down){
            shooter.SAngle.setPosition(shooter.SAngle.getPosition()-0.0008);
        }
        telemetry.addData("Pos", shooter.SAngle.getPosition());
        telemetry.update();
    }

    public void toggleFlywheel() {
        if(gamepad2.circleWasPressed()){
            shooter.toggleFlywheel();
        }
    }
    public void holdShooter(int goalId, Telemetry telemetry) {
        shooter.holdShooter(goalId, telemetry, true);
    }

    public void checkTunnelFull() {
        shooter.checkTunnelFull();
    }
}
