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

    private PathChain[] paths;
    private PathState[] states;
    private PathState pathState;
    private PathPoses[] pathPoses;
    int curPathIdx = 0;
    
    public void init(Telemetry telemetry, HardwareMap hardwareMap, Pose startPose, PathPoses[] pathPoses) {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
        follower = Constants.createFollower(hardwareMap);
        
        shooter.init(hardwareMap);
        follower.setStartingPose(startPose);

        this.pathPoses = pathPoses;
        buildPath(follower);
    }
    
    private void buildPath(Follower follower) {
        for (int i = 0; i < pathPoses.length; i++) {
            Curve curve;
            Pose[] poses = pathPoses[i].poses;
            PathPoses pathPose = pathPoses[i];

            if (pathPose.poses.length == 2) curve = new BezierLine(poses[0],poses[1]);
            else curve = new BezierCurve(poses[0],poses[1],poses[2]);

            states[i] = pathPose.type;
            paths[i] = follower
                    .pathBuilder()
                    .addPath(curve)
                    .setLinearHeadingInterpolation(pathPose.startHeading, pathPose.endHeading)
                    .build();
        }

        states[pathPoses.length] = PathState.LEAVE;
        pathState = states[0];
    }

    boolean  shotTriggered = false;
    private PathState autonomousPathUpdate() {
        switch (pathState) {
            case SHOOT:
                if(follower.isBusy()) break;
//                if(!shotTriggered) {
//                    shooter.fireShots(3, true);
//                    shotTriggered = true;
//                } else if(!shooter.isBusy()) {
//                    shotTriggered = false;
                curPathIdx++;
                pathState = states[curPathIdx];
//                }
                break;
            case PICK_UP:
                follower.followPath(paths[curPathIdx]);
                curPathIdx++;
                pathState = states[curPathIdx];
                break;
            case LAUNCH_ZONE:
                if(!follower.isBusy()) {
                    follower.followPath(paths[curPathIdx]);
                    pathState = PathState.SHOOT;
                }
                break;
            case LEAVE:
                panelsTelemetry.addData("done", "leave");
                break;
        }
        return pathState;
    }

    public void updateFollower(Telemetry telemetry) {
        follower.update();
        shooter.update();
        pathState = autonomousPathUpdate(); 

        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.update(telemetry);
    }
    
    
    
}
