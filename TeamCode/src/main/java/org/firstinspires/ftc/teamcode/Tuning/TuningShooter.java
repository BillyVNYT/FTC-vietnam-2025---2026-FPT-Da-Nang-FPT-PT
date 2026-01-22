package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.Shooter;
import org.firstinspires.ftc.teamcode.utils.SortBall;

@TeleOp
public class TuningShooter extends LinearOpMode {
    private Shooter shooter;
    private SortBall sortBall;
    int rpm = 2800;
    int velocity = (rpm*28)/60;
    boolean ShooterRun = true;
    @Override
    public void runOpMode() throws InterruptedException {
        shooter = new Shooter(hardwareMap);

        waitForStart();
        while (opModeIsActive()){
            if(gamepad1.dpadRightWasPressed() && !ShooterRun) {
                shooter.setMotorVelocity(2300, telemetry);
            } else if(gamepad1.dpadLeftWasPressed() && ShooterRun){
                shooter.setMotorVelocity(0, telemetry);
            }
            if(gamepad1.dpad_up){
                shooter.SAngle.setPosition(shooter.SAngle.getPosition()+0.0002);
            } else if(gamepad1.dpad_down){
                shooter.SAngle.setPosition(shooter.SAngle.getPosition()-0.0002);
            }
            if(gamepad1.circleWasPressed()){
//                shooter.shoot(3, shooter.spindexer, telemetry);
            }
            telemetry.addData("Pos", shooter.SAngle.getPosition());
            telemetry.update();
        }
    }
}
