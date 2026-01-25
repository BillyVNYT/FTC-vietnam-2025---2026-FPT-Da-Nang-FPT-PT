package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.LimelightHardware;
import org.firstinspires.ftc.teamcode.utils.Shooter;
import org.firstinspires.ftc.teamcode.utils.SortBall;

@TeleOp
public class TuningShooter extends LinearOpMode {
    private Shooter shooter;
    private SortBall sortBall;
    int rpm = 2800;
    int velocity = (rpm*28)/60;
    boolean ShooterRun = true;
    LimelightHardware limelightHardware;
    @Override
    public void runOpMode() throws InterruptedException {
        shooter = new Shooter(hardwareMap);
        limelightHardware = new LimelightHardware(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            if(gamepad1.dpadRightWasPressed()) {
                shooter.setMotorVelocity(1800, telemetry);
            } else if(gamepad1.dpadLeftWasPressed()){
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
