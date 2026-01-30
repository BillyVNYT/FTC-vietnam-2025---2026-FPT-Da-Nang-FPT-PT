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
    Gamepad gamepad1;
    Gamepad gamepad2;
    Motif motif;
    Lifter lifter;
    int maxTick = 234;
    int minTick = -234;

    public ManualControl(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        intake = new IntakeFPT2(hardwareMap);
        shooter = new ShooterFPT2(hardwareMap, intake);
        shootSafe = new SafeShoot(hardwareMap, intake);
        lifter = new Lifter(hardwareMap);
        this.gamepad2 = gamepad2;
        this.gamepad1 = gamepad1;
    }

    public void shootBall(Telemetry telemetry) throws InterruptedException{
        if(gamepad2.right_bumper){
            shooter.shoot(telemetry);
        } else if(gamepad2.right_trigger > 0.5) {
            shooter.setMotorVelocity(2000);
        } else {
            shooter.setMotorVelocity(1300);
        }
        if(gamepad1.right_bumper){
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
        intake.updateIntakeManual(gamepad1, gamepad2);
        intake.checkHoldBall();
    }

    public void updateShooterAngleServo(Telemetry telemetry){
        if(gamepad2.triangle){
            shootSafe.SAngle.setPosition(shootSafe.SAngle.getPosition()+0.002);
        } else if(gamepad2.cross){
            shootSafe.SAngle.setPosition(shootSafe.SAngle.getPosition()-0.002);
        }
    }

    public void holdShooter(int goalId, Telemetry telemetry, boolean Reverse) {
        if(shooter.MTurnOuttake.getCurrentPosition() > minTick && shooter.MTurnOuttake.getCurrentPosition() < maxTick) {
            if (gamepad2.dpad_right) {
                shooter.MTurnOuttake.setPower(0.25);
            } else if (gamepad2.dpad_left) {
                shooter.MTurnOuttake.setPower(-0.25);
            } else {
                shooter.holdShooter(goalId, telemetry, Reverse);
            }
        } else if(shooter.MTurnOuttake.getCurrentPosition() > minTick) {
            if(gamepad2.dpad_right){
                shooter.MTurnOuttake.setPower(0.25);
            }
        } else if(gamepad2.dpad_left){
            shooter.MTurnOuttake.setPower(-0.25);
        } else {
            shooter.MTurnOuttake.setPower(0);
        }
    }

//    public void TurnShooterControl() {
//        if(gamepad2.dpad_right){
//            shooter.MTurnOuttake.setPower(0.75);
//        } else if(gamepad2.dpad_left){
//            shooter.MTurnOuttake.setPower(-0.75);
//        } else {
//            shooter.MTurnOuttake.setPower(0);
//        }
//    }
    public void lift() {
        if(gamepad1.dpadUpWasPressed()){
            lifter.lower();
        } else if(gamepad1.dpadDownWasPressed()){
            lifter.lift();
        }
    }
}
