package org.firstinspires.ftc.teamcode.Autonomous;


import static org.firstinspires.ftc.teamcode.Autonomous.RedPathPoses.*;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
@Configurable // Panels
public class RedLowStart extends LinearOpMode{

    PathPoses[] pathPoses = {
            new PathPoses(Math.toRadians(90), RedStartToLowZonePose, GenericAuto.PathState.START),
            new PathPoses(Math.toRadians(90), RedLowZoneToLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), RedLoadZoneToSemiLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), RedSemiLoadZoneToLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), RedLoadZoneToSemiLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), RedSemiLoadZoneToLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), RedLoadZoneReturnToLowZonePose, GenericAuto.PathState.SHOOT),
            new PathPoses(Math.toRadians(90), RedLowZoneToLeavePose, GenericAuto.PathState.LEAVE)
    };
    GenericAuto auto;
    @Override
    public void runOpMode() throws InterruptedException {
        auto = new GenericAuto(telemetry, hardwareMap, StartRedPose, pathPoses, 24);
        telemetry.addLine("Initialized, waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            auto.updateFollower(telemetry);
        }
    }
}
