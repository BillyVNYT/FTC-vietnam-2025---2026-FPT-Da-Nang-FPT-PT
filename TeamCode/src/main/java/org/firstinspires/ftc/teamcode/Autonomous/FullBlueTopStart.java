package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.BluePathPoses.*;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
@Configurable // Panels
public class FullBlueTopStart extends LinearOpMode {

    Pose startPose = new Pose(64, 80, Math.toRadians(180));
    Pose goalPose = new Pose(26.787, 128.303);
    Pose[] startToTopZonePoses = {new Pose(64, 80), topZonePose};

    PathPoses[] pathPoses = {
            new PathPoses(Math.toRadians(180), startToTopZonePoses, GenericAuto.PathState.START),
            new PathPoses(Math.toRadians(180), topZoneToPickupMidPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), pickupMidToTopZonePoses, GenericAuto.PathState.SHOOT),
//            new PathPoses(Math.toRadians(180), Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
//            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.SHOOT),
//            new PathPoses(Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
//            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.SHOOT),
//            new PathPoses(Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
//            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.SHOOT),
//            new PathPoses(Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
//            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.SHOOT),
//            new PathPoses(Math.toRadians(145), Math.toRadians(180), topZoneToPickupTopPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), topZoneToPickupTopPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), pickupTopToTopZonePoses, GenericAuto.PathState.SHOOT),
            new PathPoses(Math.toRadians(180), topZoneToPickupLowPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), pickupLowToTopZonePoses, GenericAuto.PathState.SHOOT),
            new PathPoses(Math.toRadians(180), topZoneToLeavePoses, GenericAuto.PathState.LEAVE)
    };

    GenericAuto auto;

    @Override
    public void runOpMode() throws InterruptedException {

        auto = new GenericAuto(telemetry, hardwareMap, startPose, pathPoses);

        telemetry.addLine("Initialized, waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            auto.updateFollower(telemetry);
        }
    }
}
