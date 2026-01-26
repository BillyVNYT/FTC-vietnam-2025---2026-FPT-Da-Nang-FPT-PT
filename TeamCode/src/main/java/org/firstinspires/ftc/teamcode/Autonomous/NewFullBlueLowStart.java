package org.firstinspires.ftc.teamcode.Autonomous;


import static org.firstinspires.ftc.teamcode.Autonomous.NewBluePathPoses.*;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
@Configurable // Panels
public class NewFullBlueLowStart extends LinearOpMode{
    PathPoses[] pathPoses = {
            new PathPoses(Math.toRadians(90), BlueStartToLowZonePose, GenericAuto.PathState.START),
            new PathPoses(Math.toRadians(90), BlueLowZoneToLoadZonePose, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(90), BlueLoadZoneReturnToLowZonePose, GenericAuto.PathState.SHOOT),
            new PathPoses(Math.toRadians(90), BlueLowZoneToLeavePose, GenericAuto.PathState.LEAVE)
    };
    GenericAuto auto;
    @Override
    public void runOpMode() throws InterruptedException {
        auto = new GenericAuto(telemetry, hardwareMap, StartBluePose, pathPoses,20);
        telemetry.addLine("Initialized, waiting for start");
        telemetry.update();
        while (opModeIsActive() && !isStopRequested()) {
            auto.updateFollower(telemetry);
            telemetry.update();
        }
    }
}
