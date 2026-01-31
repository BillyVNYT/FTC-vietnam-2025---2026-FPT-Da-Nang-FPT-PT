package org.firstinspires.ftc.teamcode.Autonomous;

import static java.lang.Thread.sleep;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Curve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utils.Intake;
import org.firstinspires.ftc.teamcode.utils.LimelightHardware;
import org.firstinspires.ftc.teamcode.utils.Shooter;
import org.firstinspires.ftc.teamcode.utils.SortBall;

import java.util.ArrayList;
import java.util.List;

public class GenericAuto {
    public enum PathState {
        PICK_UP,
        OPEN_GATE,
        SHOOT,
        LEAVE,
        START,
        SCAN,
    }

    private final TelemetryManager panelsTelemetry;
    public Follower follower;
    public Shooter shooter;
    private final SortBall spindexer;
    private final LimelightHardware limelight;
    private final Intake intake;
    private final List<PathChain> paths = new ArrayList<>();
    private final List<PathState> states = new ArrayList<>();;
    private PathState currentState ;
    private int curPathIdx = 0;
    boolean  shotTriggered = false;
    int goalId;

    boolean spindexerReversed;
    private ElapsedTime autoTimer;
    private static final double AUTO_DURATION = 30.0;
    private static final double SORT_TIME_THRESHOLD = 10.0;


    public GenericAuto(Telemetry telemetry, HardwareMap hardwareMap, Pose startPose, PathPoses[] pathPoses, int goalId) {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap, true, telemetry, goalId == 24);
        limelight = new LimelightHardware(hardwareMap);
        spindexer = new SortBall(hardwareMap, shooter, goalId == 24);
        this.goalId = goalId;
        spindexerReversed = goalId == 24;

        buildPaths(follower, pathPoses);

        if (!states.isEmpty()) {
            currentState = states.get(0);
            follower.followPath(paths.get(0));
        } else currentState = PathState.LEAVE;

        panelsTelemetry.update(telemetry);
        this.autoTimer = new ElapsedTime();

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    public void resetTimer(){
        autoTimer.reset();
    }

    private void buildPaths(Follower follower, PathPoses[] pathPoses) {
        for (PathPoses pathSegment : pathPoses) {
            Curve curve;
            if (pathSegment.poses.length == 2) {
                curve = new BezierLine(pathSegment.poses[0], pathSegment.poses[1]);
            } else if (pathSegment.poses.length == 3) {
                curve = new BezierCurve(pathSegment.poses[0], pathSegment.poses[1], pathSegment.poses[2]);
            } else {
                curve = new BezierCurve(pathSegment.poses[0], pathSegment.poses[1], pathSegment.poses[2], pathSegment.poses[3]);
            }

            PathBuilder pathBuilder = follower.pathBuilder().addPath(curve);
            if(pathSegment.endHeading < 0) pathBuilder.setConstantHeadingInterpolation(pathSegment.startHeading);
            else pathBuilder.setLinearHeadingInterpolation(pathSegment.startHeading, pathSegment.endHeading);

            paths.add(pathBuilder.build());
            states.add(pathSegment.type);
        }
    }
    private void autonomousPathUpdate(Telemetry telemetry) throws InterruptedException {

        switch (currentState) {
            case SHOOT:
                if (follower.isBusy()) {
                    break;
                }

                if (!shotTriggered) {
                    shooter.shoot(3, spindexer, 2400, true);
                    shotTriggered = true;
                    break;
                }

                 if (!shooter.isBusy()) {
                    shotTriggered = false;
//
//                    if(limelight.getMotif() != null && AUTO_DURATION - autoTimer.seconds() <= 15) {
//                        currentState = PathState.SCAN;
//                        break;
//                    }

                    intake.start();
                    double spindexerInitPos = spindexerReversed ? spindexer.INTAKE_SLOT_POS2[0] : spindexer.INTAKE_SLOT_POS[0];
                    spindexer.controlSpindexer(spindexerInitPos);
                    goToNextPath();
                 }
                break;

            case PICK_UP:
                if (follower.isBusy()) {
                    spindexer.autoLoadBallsIn(telemetry, spindexerReversed);
                    break;
                }

                PathState nextState = goToNextPath();

                if (nextState != PathState.PICK_UP) {
//                    boolean shouldSort = AUTO_DURATION - autoTimer.seconds() <= SORT_TIME_THRESHOLD;
                    spindexer.readyToShoot(false, telemetry);
                    intake.stop();
                }
                break;

            case SCAN:
                if(limelight.getMotif() != null) {
                    shooter.updateMTurnOuttakePos(0);
                    limelight.getAprilTagData(telemetry);
                } else {
                    shooter.updateMTurnOuttakePos(169);
                    goToNextPath();
                }
                break;

            case START:
                shooter.toggleFlywheel();
                spindexer.readyToShoot(false, telemetry);
                PathChain currentPath = paths.get(curPathIdx);

                follower.followPath(currentPath);
                currentState = PathState.SHOOT;
                break;

            case OPEN_GATE:
                if(follower.isBusy()) break;
                goToNextPath();
                break;

            case LEAVE:
                panelsTelemetry.addData("done", "leave");
                break;
        }
    }

    private PathState goToNextPath() {
        curPathIdx++;
        if (curPathIdx >= paths.size()) return PathState.LEAVE;

        currentState = states.get(curPathIdx);
        PathChain currentPath = paths.get(curPathIdx);
        boolean isPickingUp = currentState == PathState.PICK_UP;
        follower.followPath(currentPath, isPickingUp ? 0.55 : 1, true);
        return currentState;
    }

    public void updateFollower(Telemetry telemetry) throws InterruptedException{
        follower.update();
        autonomousPathUpdate(telemetry);

        panelsTelemetry.debug("Path State", currentState);
        panelsTelemetry.debug("Path Index", curPathIdx);
        panelsTelemetry.update(telemetry);
    }
    
}
