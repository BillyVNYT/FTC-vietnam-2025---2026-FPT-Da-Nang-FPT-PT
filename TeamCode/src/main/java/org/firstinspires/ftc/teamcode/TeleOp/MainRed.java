package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.DriveTrain;
import org.firstinspires.ftc.teamcode.ManualControl;
import org.firstinspires.ftc.teamcode.Outtake;

public class MainRed extends LinearOpMode {
    private DriveTrain driveTrain = new DriveTrain(hardwareMap);
    private Outtake outtake = new Outtake(hardwareMap, telemetry);
    private ManualControl manualControl = new ManualControl(hardwareMap, gamepad1, gamepad2);
    private boolean AutoHold = true;
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (opModeIsActive()){
            driveTrain.DrivetrainControlAdvanced(gamepad1, gamepad2);
            if(gamepad1.right_bumper){
                outtake.Shoot();
            }
            if(gamepad1.start){
                AutoHold = true;
            }
            if(AutoHold){
                outtake.holdOutTake();
            } else {
                manualControl.ControlTurnOutTake();
            }
        }
    }
}
