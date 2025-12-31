package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.DriveTrain;

public class MainRed extends LinearOpMode {
    private DriveTrain driveTrain = new DriveTrain(hardwareMap);
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (opModeIsActive()){
            driveTrain.DrivetrainControlAdvanced(gamepad1, gamepad2);
        }
    }
}
