package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.Intake;
import org.firstinspires.ftc.teamcode.utils.IntakeFPT2;
import org.firstinspires.ftc.teamcode.utils.Lifter;
import org.firstinspires.ftc.teamcode.utils.Motif;
import org.firstinspires.ftc.teamcode.utils.SafeShoot;
import org.firstinspires.ftc.teamcode.utils.ShooterFPT2;

public class ManualControl {

    ShooterFPT2 shooter;
    SafeShoot shootSafe;
    IntakeFPT2 intake;
    Lifter lifter;
    Gamepad gamepad2;
    Motif motif;

    public ManualControl(HardwareMap hardwareMap, Gamepad gamepad) {
//        lifter = new Lifter(hardwareMap);
        intake = new IntakeFPT2(hardwareMap);
        shooter = new ShooterFPT2(hardwareMap, intake);
        shootSafe = new SafeShoot(hardwareMap, intake);
        gamepad2 = gamepad;
//        motif = new Motif(hardwareMap);
    }

    public void shootBall(Telemetry telemetry) throws InterruptedException{
        if(gamepad2.cross){
            shooter.shoot(telemetry);
        } else {
            shooter.setMotorVelocity(0);
        }

        if(gamepad2.dpad_up){
            intake.shoot = true;
            intake.HoldBall = false;
            intake.checkHoldBall();
            intake.isActive();
        } else {
            intake.shoot = false;
            intake.HoldBall = true;
            intake.checkHoldBall();
            intake.stop();
        }
    }

    public void toggleIntake(){
        intake.updateIntakeManual(gamepad2);
        intake.checkHoldBall();
    }

    public void updateShooterAngleServo(Telemetry telemetry){
        if(gamepad2.circle){
            shootSafe.SAngle.setPosition(shootSafe.SAngle.getPosition()+0.0008);
        } else if(gamepad2.triangle){
            shootSafe.SAngle.setPosition(shootSafe.SAngle.getPosition()-0.0008);
        }
        telemetry.addData("Pos", shootSafe.SAngle.getPosition());
        telemetry.update();
    }

    public void holdShooter(int goalId, Telemetry telemetry, boolean Reverse) {
        shooter.holdShooter(goalId, telemetry, Reverse);
    }

//    public void toggleFlywheel() {
//        if(gamepad2.circleWasPressed()){
//            shooter.toggleFlywheel();
//        }
//    }

//    public void checkTunnelFull() {
//        shooter.checkTunnelFull();
//    }
}
