package org.firstinspires.ftc.teamcode.Autonomous;


import static org.firstinspires.ftc.teamcode.Autonomous.NewBluePathPoses.*;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.opencv.core.Mat;

@Autonomous
@Configurable // Panels
public class NewFullBlue extends LinearOpMode{
    PathPoses[] pathPoses = {
            new PathPoses(Math.toRadians(180), StartToShoot, GenericAuto.PathState.START),
            new PathPoses(Math.toRadians(180), TopZoneToPickupMid, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), PickupMidToBackward, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(139), BackwardToBlueGate, GenericAuto.PathState.OPEN_GATE),
            new PathPoses(Math.toRadians(180), BlueGateToTopZone,GenericAuto.PathState.SHOOT),
            new PathPoses(Math.toRadians(180), TopZoneToPickUpTop, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), PickUpTopToTopZone, GenericAuto.PathState.SHOOT),
            new PathPoses(Math.toRadians(180), TopZoneToPickUpLow, GenericAuto.PathState.PICK_UP),
            new PathPoses(Math.toRadians(180), PickUpLowToTopZone, GenericAuto.PathState.LEAVE),
//            new PathPoses(Math.toRadians(90), BlueLowZoneToLeavePose, GenericAuto.PathState.LEAVE)
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
