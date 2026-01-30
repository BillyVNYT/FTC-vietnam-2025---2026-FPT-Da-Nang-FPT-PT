package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Disabled
@TeleOp(name = "IMU FULL TEST", group = "Test")
public class IMUTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {

        IMU imu = hardwareMap.get(IMU.class, "imu");

        // 👉 THỬ ĐỔI 2 DÒNG NÀY NẾU CẦN
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        );

        imu.initialize(parameters);

        telemetry.addLine("IMU initialized");
        telemetry.addLine("Xoay robot de test Yaw / Pitch / Roll");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // Góc
            YawPitchRollAngles angles = imu.getRobotYawPitchRollAngles();

            telemetry.addData("Yaw (Z)", angles.getYaw(AngleUnit.DEGREES));
            telemetry.addData("Pitch (Y)", angles.getPitch(AngleUnit.DEGREES));
            telemetry.addData("Roll (X)", angles.getRoll(AngleUnit.DEGREES));

            // Vận tốc góc (quan trọng cho Road Runner)
            AngularVelocity vel = imu.getRobotAngularVelocity(AngleUnit.DEGREES);

            telemetry.addLine("---- Angular Velocity ----");
            telemetry.addData("wx (roll rate)", vel.xRotationRate);
            telemetry.addData("wy (pitch rate)", vel.yRotationRate);
            telemetry.addData("wz (yaw rate)", vel.zRotationRate);

            telemetry.update();
        }
    }
}
