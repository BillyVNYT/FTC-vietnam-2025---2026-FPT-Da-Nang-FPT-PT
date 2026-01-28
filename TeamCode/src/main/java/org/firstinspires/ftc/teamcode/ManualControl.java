package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.IntakeFPT2;
import org.firstinspires.ftc.teamcode.utils.Lifter;
import org.firstinspires.ftc.teamcode.utils.ShooterFPT2;

public class ManualControl {

    ShooterFPT2 shooter;
    IntakeFPT2 intake;
    Lifter lifter;
    Gamepad gamepad2;

    public ManualControl(HardwareMap hardwareMap, Gamepad gamepad) {
//        lifter = new Lifter(hardwareMap);
        intake = new IntakeFPT2(hardwareMap);
        shooter = new ShooterFPT2(hardwareMap, intake);
        gamepad2 = gamepad;
    }

    public void shootBall(Telemetry telemetry) throws InterruptedException{
        if(gamepad2.right_bumper && !shooter.isBusy()){
            shooter.shoot(telemetry);
            Thread.sleep(2000);
            intake.HoldBall = false;
            intake.checkHoldBall();
            intake.isActive();
        } else {
            intake.HoldBall = true;
            shooter.MShooter1.setPower(0);
            shooter.MShooter2.setPower(0);
            intake.stop();
        }
    }

    public void toggleIntake(){
        intake.updateIntakeManual(gamepad2);
        intake.checkHoldBall();
    }

//    public void updateShooterAngleServo(Telemetry telemetry){
//        if(gamepad2.dpad_up){
//            shooter.SAngle.setPosition(shooter.SAngle.getPosition()+0.0008);
//        } else if(gamepad2.dpad_down){
//            shooter.SAngle.setPosition(shooter.SAngle.getPosition()-0.0008);
//        }
//        telemetry.addData("Pos", shooter.SAngle.getPosition());
//        telemetry.update();
//    }

//    public void toggleFlywheel() {
//        if(gamepad2.circleWasPressed()){
//            shooter.toggleFlywheel();
//        }
//    }

//    public void checkTunnelFull() {
//        shooter.checkTunnelFull();
//    }
}
