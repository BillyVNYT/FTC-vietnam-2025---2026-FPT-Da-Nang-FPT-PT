package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class SubRed extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SubRobot robot = new SubRobot(24, hardwareMap, gamepad2);
        waitForStart();
        while (opModeIsActive()){
            robot.opMode(telemetry);
        }
    }

}
