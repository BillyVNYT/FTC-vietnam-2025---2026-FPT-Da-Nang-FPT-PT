package org.firstinspires.ftc.teamcode.Autonomous;


import static org.firstinspires.ftc.teamcode.Autonomous.BluePathPoses.*;
import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous
@Configurable // Panels
public class FullBlueTopStart extends LinearOpMode {
    Pose startPose = new Pose(27.48929159802308, 127.79571663920923, Math.toRadians(180));
    Pose goalPose = new Pose(26.787, 128.303);
    GenericAuto auto;
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

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        auto = new GenericAuto(telemetry, hardwareMap, startPose, pathPoses);
        waitForStart();
        while(opModeIsActive()){
            auto.updateFollower(telemetry);
            telemetry.addData("Status", "running");
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
//    }

}
