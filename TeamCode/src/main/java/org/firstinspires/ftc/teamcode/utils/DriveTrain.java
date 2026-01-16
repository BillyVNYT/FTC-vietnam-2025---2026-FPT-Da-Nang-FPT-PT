package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DriveTrain {
    DcMotor RightFrontDrive;
    DcMotor RightBackDrive;
    DcMotor LeftFrontDrive;
    DcMotor LeftBackDrive;
    IMU imu;
    public DriveTrain(HardwareMap hardwareMap){
        RightFrontDrive = hardwareMap.get(DcMotor.class, "m1");
        RightBackDrive = hardwareMap.get(DcMotor.class, "m2");
        LeftFrontDrive = hardwareMap.get(DcMotor.class, "m3");
        LeftBackDrive = hardwareMap.get(DcMotor.class, "m4");

        LeftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        RightBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);
    }

    public void drivetrainControlBasic(Gamepad gamepad1){
        double y = (-gamepad1.left_stick_y - gamepad1.right_stick_y);
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        LeftFrontDrive.setPower(y + x + rx);
        LeftBackDrive.setPower(y - x - rx);
        RightFrontDrive.setPower(y - x + rx);
        RightBackDrive.setPower(y + x - rx);
    }

    public void noTurnDrivetrainControl(Gamepad gamepad1){
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;

        LeftFrontDrive.setPower(y + x);
        LeftBackDrive.setPower(y - x);
        RightFrontDrive.setPower(y - x);
        RightBackDrive.setPower(y + x);
    }

    public void drivetrainControlAdvanced(Gamepad gamepad1){
        double y = (-gamepad1.left_stick_y - gamepad1.right_stick_y);
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;
        if (gamepad1.options) {
            imu.resetYaw();
        }

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        LeftFrontDrive.setPower(frontLeftPower);
        LeftBackDrive.setPower(backLeftPower);
        RightFrontDrive.setPower(frontRightPower);
        RightBackDrive.setPower(backRightPower);
    }
}
