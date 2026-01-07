package org.firstinspires.ftc.teamcode.Autonomous;


import static org.firstinspires.ftc.teamcode.Autonomous.BluePathPoses.*;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


@Autonomous
@Configurable // Panels
public class FullBlueTopStart extends OpMode {
    Pose startPose = new Pose(26.787, 128.303, Math.toRadians(180));
    Pose goalPose = new Pose(26.787, 128.303);
    Pose[] startToTopZonePoses = {goalPose, topZonePose};

    PathPoses[] pathPoses = {new PathPoses(Math.toRadians(180), startToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(180), topZoneToPickupMidPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), pickupMidToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(180), Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(145), Math.toRadians(180), topZoneToPickupTopPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), pickupTopToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(180), topZoneToPickupLowPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), pickupLowToLowZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(180), lowZoneToLeavePoses, GenericAuto.PathState.LEAVE)
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
