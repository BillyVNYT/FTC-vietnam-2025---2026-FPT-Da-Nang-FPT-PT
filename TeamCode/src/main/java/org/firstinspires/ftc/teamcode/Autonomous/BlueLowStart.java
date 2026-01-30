package org.firstinspires.ftc.teamcode.Autonomous;


import static org.firstinspires.ftc.teamcode.Autonomous.NewBluePathPoses.*;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
@Configurable // Panels
public class BlueLowStart extends LinearOpMode{
    PathConstraints slow = new PathConstraints(20, 20, 20, 20);

    PathPoses[] pathPoses = {
            new PathPoses(Math.toRadians(90), BlueStartToLowZonePose, GenericAuto.PathState.START),
            new PathPoses(Math.toRadians(90), BlueLowZoneToLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), BlueLoadZoneToSemiLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), BlueSemiLoadZoneToLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), BlueLoadZoneToSemiLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), BlueSemiLoadZoneToLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), BlueLoadZoneReturnToLowZonePose, GenericAuto.PathState.SHOOT),
            new PathPoses(Math.toRadians(90), BlueLowZonePoseToPickUpLow, GenericAuto.PathState.PICK_UP).setConstraints(slow),
            new PathPoses(Math.toRadians(90), BluePickUpLowToLowZonePose,GenericAuto.PathState.SHOOT),
            new PathPoses(Math.toRadians(90), BlueLowZoneToLeavePose, GenericAuto.PathState.LEAVE)
    };
    GenericAuto auto;
    @Override
    public void runOpMode() throws InterruptedException {
        auto = new GenericAuto(telemetry, hardwareMap, StartBluePose, pathPoses,20);
        telemetry.addLine("Initialized, waiting for start");
        telemetry.update();
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            auto.updateFollower(telemetry);
            telemetry.update();
        }
    }
}