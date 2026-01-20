package org.firstinspires.ftc.teamcode.Autonomous;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Curve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utils.Intake;
import org.firstinspires.ftc.teamcode.utils.Motif;
import org.firstinspires.ftc.teamcode.utils.Shooter;
import org.firstinspires.ftc.teamcode.utils.SortBall;

import java.util.ArrayList;
import java.util.List;

public class GenericAuto {
    public enum PathState {
        PICK_UP,
        SHOOT,
        LEAVE,
        START,
    }
    private final TelemetryManager panelsTelemetry;
    public Follower follower;
    public Shooter shooter;
    private final SortBall spindexer;
    private final Motif motif;
    private final Intake intake;
    private final List<PathChain> paths = new ArrayList<>();
    private final List<PathState> states = new ArrayList<>();;
    private PathState currentState ;
    private int curPathIdx = 0;
    boolean  shotTriggered = false;

    public GenericAuto(Telemetry telemetry, HardwareMap hardwareMap, Pose startPose, PathPoses[] pathPoses) {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        motif = new Motif(hardwareMap);
        spindexer = new SortBall(motif.getSampleMotif(), hardwareMap);

        buildPaths(follower, pathPoses);

        if (!states.isEmpty()) {
            currentState = states.get(0);
            follower.followPath(paths.get(0));
        } else currentState = PathState.LEAVE;

        panelsTelemetry.update(telemetry);
    }
    
    private void buildPaths(Follower follower, PathPoses[] pathPoses) {
        for (PathPoses pathSegment : pathPoses) {
            Curve curve;
            if (pathSegment.poses.length == 2) {
                curve = new BezierLine(pathSegment.poses[0], pathSegment.poses[1]);
            } else {
                curve = new BezierCurve(pathSegment.poses[0], pathSegment.poses[1], pathSegment.poses[2]);
            }

            PathBuilder pathBuilder = follower.pathBuilder().addPath(curve);
            if(pathSegment.endHeading < 0) pathBuilder.setConstantHeadingInterpolation(pathSegment.startHeading);
            else pathBuilder.setLinearHeadingInterpolation(pathSegment.startHeading, pathSegment.endHeading);

            paths.add(pathBuilder.build());
            states.add(pathSegment.type);
        }
    }

    private void autonomousPathUpdate(Telemetry telemetry) throws InterruptedException {
        if (follower.isBusy()) {
            return; // Wait for the current path to complete
        }

        switch (currentState) {
            case SHOOT:
                if (!shotTriggered) {
                    shooter.shoot(3, spindexer, telemetry);
                    shotTriggered = true;
                }

                 if (!shooter.isBusy()) {
                    shotTriggered = false;
                    intake.start();
                    goToNextPath();
                 }
                break;

            case PICK_UP:
                intake.stop();
                spindexer.readyToShoot(false, telemetry);
                goToNextPath();
                break;
            case START:
                PathChain currentPath = paths.get(curPathIdx);
                follower.followPath(currentPath);
                currentState = PathState.SHOOT;
            case LEAVE:
                panelsTelemetry.addData("done", "leave");
                break;
        }
    }

    private void goToNextPath() {
        curPathIdx++;
        if (curPathIdx >= paths.size()) return;

        currentState = states.get(curPathIdx);
        PathChain currentPath = paths.get(curPathIdx);
        boolean isPickingUp = currentState == PathState.PICK_UP;
        follower.followPath(currentPath, isPickingUp ? 0.65 : 1, true);
    }

    public void updateFollower(Telemetry telemetry) throws InterruptedException{
        follower.update();
        autonomousPathUpdate(telemetry);

        panelsTelemetry.debug("Path State", currentState);
        panelsTelemetry.debug("Path Index", curPathIdx);
        panelsTelemetry.update(telemetry);
    }
    
    
    
}
