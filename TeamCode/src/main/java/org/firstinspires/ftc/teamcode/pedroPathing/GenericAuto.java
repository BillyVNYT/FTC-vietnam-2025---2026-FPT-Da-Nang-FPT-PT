package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Curve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;

public class GenericAuto {
    public enum PathState {
        PICK_UP,
        LAUNCH_ZONE,
        SHOOT,
        LEAVE,
    }
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private ShooterLogic shooter;

    private List<PathChain> paths;
    private List<PathState> states;
    private PathState currentState;
    private int curPathIdx = 0;
    boolean  shotTriggered = false;

    public void init(Telemetry telemetry, HardwareMap hardwareMap, Pose startPose, PathPoses[] pathPoses) {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        shooter = new ShooterLogic();
        shooter.init(hardwareMap);

        buildPaths(follower, pathPoses);

        if (!states.isEmpty()) {
            currentState = states.get(0);
            follower.followPath(paths.get(0));
        } else currentState = PathState.LEAVE;


        panelsTelemetry.update(telemetry);
    }
    
    private void buildPaths(Follower follower, PathPoses[] pathPoses) {
        paths = new ArrayList<>();
        states = new ArrayList<>();

        for (PathPoses pathSegment : pathPoses) {
            Curve curve;
            if (pathSegment.poses.length == 2) {
                curve = new BezierLine(pathSegment.poses[0], pathSegment.poses[1]);
            } else {
                curve = new BezierCurve(pathSegment.poses[0], pathSegment.poses[1], pathSegment.poses[2]);
            }

            states.add(pathSegment.type);
            paths.add(follower.pathBuilder()
                    .addPath(curve)
                    .setLinearHeadingInterpolation(pathSegment.startHeading, pathSegment.endHeading)
                    .build());
        }
    }

    private void autonomousPathUpdate() {
        if (follower.isBusy()) {
            return; // Wait for the current path to complete
        }

        switch (currentState) {
            case SHOOT:
                if (!shotTriggered) {
                    // shooter.fireShots(3, true); // Example
                    shotTriggered = true;
                }

                // if (!shooter.isBusy()) { // Uncomment when shooter logic is ready
                shotTriggered = false;
                goToNextPath();
                // }
                break;

            case PICK_UP:
                goToNextPath();
                break;
            case LAUNCH_ZONE:
                currentState = PathState.SHOOT;
                goToNextPath();
                break;
            case LEAVE:
                panelsTelemetry.addData("done", "leave");
                break;
        }
    }

    private void goToNextPath() {
        curPathIdx++;
        if (curPathIdx < paths.size()) {
            currentState = states.get(curPathIdx);
            PathChain currentPath = paths.get(curPathIdx);
            follower.followPath(currentPath);
        } else currentState = PathState.LEAVE;
    }

    public void updateFollower(Telemetry telemetry) {
        follower.update();
        shooter.update();
        autonomousPathUpdate();

        panelsTelemetry.debug("Path State", currentState);
        panelsTelemetry.debug("Path Index", curPathIdx);
        panelsTelemetry.update(telemetry);
    }
    
    
    
}
