package org.firstinspires.ftc.teamcode.Autonomous;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous
@Configurable // Panels
public class LowRed extends LinearOpMode {
    Pose startPose = new Pose(80, 16, Math.toRadians(90));
    Pose lowZonePose = new Pose(81.115, 5.735);
    Pose pickup1Pose = new Pose(121.468, 36.871);
    Pose pickup2Pose = new Pose(121.468, 82.959);
    Pose highZonePose = new Pose(77.838, 77.838);

    Pose[] lowZoneToPickup1Poses = {lowZonePose, new Pose(90.000, 35.000), pickup1Pose};
    Pose[] pickup1ToLowScorePoses = {pickup1Pose, lowZonePose};
    Pose[] lowZoneToPickup2Poses = {lowZonePose, new Pose(90.000, 60.000), pickup2Pose};
    Pose[] pickup2ToHighScorePoses = {pickup2Pose, highZonePose};
    PathPoses[] pathPoses = {new PathPoses(Math.toRadians(90), Math.toRadians(0), lowZoneToPickup1Poses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(0), Math.toRadians(120), pickup1ToLowScorePoses, GenericAuto.PathState.SHOOT),
            new PathPoses(Math.toRadians(120), Math.toRadians(0), lowZoneToPickup2Poses, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(0), Math.toRadians(135), pickup2ToHighScorePoses, GenericAuto.PathState.SHOOT)
    };

    GenericAuto auto;


    @Override
    public void runOpMode() throws InterruptedException {
        auto = new GenericAuto(telemetry, hardwareMap, startPose, pathPoses);
        waitForStart();

        while (opModeIsActive()) {
            auto.updateFollower(telemetry);
        }
    }

}
