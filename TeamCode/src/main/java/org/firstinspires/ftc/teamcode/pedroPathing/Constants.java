package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.TwoWheelConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(5.8)
            .forwardZeroPowerAcceleration(-34.976553116239685)
            .lateralZeroPowerAcceleration(-51.19052649930167)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.035, 0, 0.0000345, 0.00125))
            .headingPIDFCoefficients(new PIDFCoefficients(0.65, 0, 0.03275, 0.025))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.025, 0, 0.00001, 0.6, 0.01))
            .centripetalScaling(0.0005);

    public static PathConstraints pathConstraints = new PathConstraints(0.99,
            100,
            1,
            1);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("right_front_drive")
            .rightRearMotorName("right_back_drive")
            .leftFrontMotorName("left_front_drive")
            .leftRearMotorName("left_back_drive")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .xVelocity(63.48085756259026)
            .yVelocity(55.450629737646384);

    public static TwoWheelConstants localizerConstants = new TwoWheelConstants()
            .forwardEncoder_HardwareMapName("right_front_drive")
            .strafeEncoder_HardwareMapName("right_back_drive")
            .forwardEncoderDirection(Encoder.REVERSE)
            .strafeEncoderDirection(Encoder.FORWARD)
            .IMU_HardwareMapName("imu")
            .IMU_Orientation(
                    new RevHubOrientationOnRobot(
                            RevHubOrientationOnRobot.LogoFacingDirection.UP,
                            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                    )
            );

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .twoWheelLocalizer(localizerConstants)
                .build();
    }
}
