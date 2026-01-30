package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class TuneOuttake extends LinearOpMode {
    DcMotorEx MTurnOuttake;

    @Override
    public void runOpMode() throws InterruptedException {
        MTurnOuttake = hardwareMap.get(DcMotorEx.class, "m4");
        MTurnOuttake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MTurnOuttake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        MTurnOuttake.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("MTurnOuttake", MTurnOuttake.getCurrentPosition());
            telemetry.update();
        }
    }
}
