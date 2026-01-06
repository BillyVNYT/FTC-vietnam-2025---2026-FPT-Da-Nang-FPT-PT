package org.firstinspires.ftc.teamcode.Autonomous;


import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous
@Configurable // Panels
public class FullBlueTopStart extends OpMode {
    Pose startPose = new Pose(26.787, 128.303, Math.toRadians(90));
    Pose topZonePose = new Pose(63.810, 70.294);
    Pose pickupMidPose = new Pose(8.531, 59.204);
    Pose pickupGatePose = new Pose(11.261, 60.057);
    Pose pickupTopPose = new Pose(13.649, 83.943);
    Pose pickupLowPose = new Pose(8.531, 36.0);
    Pose lowZonePose = new Pose(59.545, 8.531);
    Pose leavePose= new Pose(21.327, 69.441);

    Pose[] startToTopZonePoses = {startPose, topZonePose};
    Pose[] topZoneToPickupMidPoses = {topZonePose, new Pose(58.692, 54.768), pickupMidPose};
    Pose[] pickupMidToTopZonePoses = {pickupMidPose, topZonePose};
    Pose[] topZoneToPickupGatePoses = {topZonePose, pickupGatePose};
    Pose[] pickupGateToTopZonePoses = {pickupGatePose, topZonePose};
    Pose[] topZoneToPickupTopPoses = {topZonePose, new Pose(42.18, 88.597), pickupTopPose};
    Pose[] pickupTopToTopZonePoses = {pickupTopPose, topZonePose};
    Pose[] topZoneToPickupLowPoses = {topZonePose, new Pose(63.299, 25.934), pickupLowPose};
    Pose[] pickupLowToLowZonePoses = {pickupLowPose, lowZonePose};
    Pose[] lowZoneToLeavePoses = {lowZonePose, leavePose};

    PathPoses[] pathPoses = {new PathPoses(Math.toRadians(90), startToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(90), topZoneToPickupMidPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), pickupMidToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(90), Math.toRadians(55), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(55), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(55), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(55), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(55), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(55), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(55), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(55), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(55), Math.toRadians(90), topZoneToPickupTopPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), pickupTopToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(90), topZoneToPickupLowPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), pickupLowToLowZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(90), lowZoneToLeavePoses, GenericAuto.PathState.LEAVE)
    };

    GenericAuto auto;

    @Override
    public void init() {
        auto = new GenericAuto(telemetry, hardwareMap, startPose, pathPoses);
    }

    @Override
    public void loop() {
        auto.updateFollower(telemetry);
    }

}
