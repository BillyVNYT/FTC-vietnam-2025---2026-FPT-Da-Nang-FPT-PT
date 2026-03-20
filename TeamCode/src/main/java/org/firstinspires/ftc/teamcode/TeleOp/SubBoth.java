package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="SubBoth")
public class SubBoth extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SubRobot robot = new SubRobot(0, hardwareMap, gamepad1, gamepad2);
        waitForStart();
//        robot.manageShootBallThread(telemetry);
        while (opModeIsActive()){
            robot.opMode(telemetry);
        }
    }

}