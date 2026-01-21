package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.utils.Shooter;

public class TuningShooter extends LinearOpMode {
    private Shooter shooter;
    int rpm = 2800;
    int velocity = (rpm*28)/60;
    @Override
    public void runOpMode() throws InterruptedException {
        shooter = new Shooter(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            shooter.setMotorVelocity(velocity, telemetry);
            if(gamepad1.dpad_up){
                shooter.SAngle.setPosition(shooter.SAngle.getPosition()+0.005);
            } else if(gamepad1.dpad_down){
                shooter.SAngle.setPosition(shooter.SAngle.getPosition()-0.005);
            }
            telemetry.addData("Pos", shooter.SAngle.getPosition());
            telemetry.update();
        }
    }
}
