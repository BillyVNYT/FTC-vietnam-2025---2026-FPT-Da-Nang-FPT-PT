package org.firstinspires.ftc.teamcode.Autonomous;


import static org.firstinspires.ftc.teamcode.Autonomous.BluePathPoses.*;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
@Configurable // Panels
public class TwoLowBlue extends LinearOpMode {
    Pose startPose = new Pose(53.062, 18.085, Math.toRadians(180));
    Pose[] startToPickupMidPoses = {lowZonePose, pickupMidPose};
    GenericAuto auto;
    PathPoses[] pathPoses = {new PathPoses(Math.toRadians(180), startToPickupMidPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), pickupMidToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(180), Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(145), topZoneToPickupGatePoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(145), pickupGateToTopZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(145), Math.toRadians(180), topZoneToPickupLowPoses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), pickupLowToLowZonePoses, GenericAuto.PathState.LAUNCH_ZONE),
            new PathPoses(Math.toRadians(180), lowZoneToLeavePoses, GenericAuto.PathState.LEAVE)
    };
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        auto = new GenericAuto(telemetry, hardwareMap, startPose, pathPoses);
        waitForStart();
        while(opModeIsActive()){
            auto.updateFollower(telemetry);
            telemetry.addData("Status", "Running");
            telemetry.update();
        }

    }



//    @Override
//    public void init() {
//        auto = new GenericAuto(telemetry, hardwareMap, startPose, pathPoses);
//    }
//
//    @Override
//    public void loop() {
//        auto.updateFollower(telemetry);
//    }

}
