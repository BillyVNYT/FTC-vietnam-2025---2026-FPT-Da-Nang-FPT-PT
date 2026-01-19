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
            .mass(15)
            .forwardZeroPowerAcceleration(-32.4181803122647)
            .lateralZeroPowerAcceleration(-53.25190569981471)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.25, 0.00025, 0.005, 0.0001))
            .headingPIDFCoefficients(new PIDFCoefficients(0.75, 0, 0.01, 0.005))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.025, 0, 0.000015, 0.6, 0.01))
            .centripetalScaling(0.00041);



    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("m1")
            .rightRearMotorName("m2")
            .leftRearMotorName("m3")
            .leftFrontMotorName("m4")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .xVelocity(64.57876903369694)
            .yVelocity(51.72019384627498);
    public static TwoWheelConstants localizerConstants = new TwoWheelConstants()
            .forwardEncoder_HardwareMapName("m1")
            .strafeEncoder_HardwareMapName("m4")
            .IMU_HardwareMapName("imu")
//            .strafeEncoderDirection(Encoder.REVERSE)
            .forwardEncoderDirection(Encoder.REVERSE)
            .forwardPodY(6.9862204724)
            .strafePodX(-6.9862204724)
            .IMU_Orientation(
                    new RevHubOrientationOnRobot(
                            RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                            RevHubOrientationOnRobot.UsbFacingDirection.UP
                    )
            );
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .twoWheelLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .build();
    }
}