//package org.firstinspires.ftc.teamcode.Autonomous;
//
//import static org.firstinspires.ftc.teamcode.Autonomous.BluePathPoses.*;
//
//import com.bylazar.configurables.annotations.Configurable;
//import com.pedropathing.geometry.Pose;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//@Autonomous
//@Configurable // Panels
//public class FullBlueTopStart extends LinearOpMode {
//
//    Pose startPose = new Pose(26.787, 128.303, Math.toRadians(90));
//    Pose goalPose = new Pose(26.787, 128.303);
//    Pose[] startToTopZonePoses = {goalPose, topZonePose};
//
//    PathPoses[] pathPoses = {
//            new PathPoses(Math.toRadians(90), startToTopZonePoses, GenericAuto.PathState.START),
//            new PathPoses(Math.toRadians(90), topZoneToPickupMidPoses, GenericAuto.PathState.PICK_UP),
//            new PathPoses(Math.toRadians(90), Math.toRadians(55), pickupMidToOpenGatePoses, GenericAuto.PathState.OPEN_GATE),
//            new PathPoses(Math.toRadians(55), Math.toRadians(90), openGateToTopZonePoses, GenericAuto.PathState.SHOOT),
//            new PathPoses(Math.toRadians(90), topZoneToPickupTopPoses, GenericAuto.PathState.PICK_UP),
//            new PathPoses(Math.toRadians(90), pickupTopToTopZonePoses, GenericAuto.PathState.SHOOT),
//            new PathPoses(Math.toRadians(90), topZoneToPickupLowPoses, GenericAuto.PathState.PICK_UP),
//            new PathPoses(Math.toRadians(90), pickupLowToTopZonePoses, GenericAuto.PathState.SHOOT),
//            new PathPoses(Math.toRadians(90), topZoneToLeavePoses, GenericAuto.PathState.LEAVE)
//    };
//
//    GenericAuto auto;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//
//        auto = new GenericAuto(telemetry, hardwareMap, startPose, pathPoses, 20);
//
//        telemetry.addLine("Initialized, waiting for start");
//        telemetry.update();
//
//        waitForStart();
//        auto.resetTimer();
//
//        while (opModeIsActive() && !isStopRequested()) {
//            auto.updateFollower(telemetry);
//        }
//    }
//}
