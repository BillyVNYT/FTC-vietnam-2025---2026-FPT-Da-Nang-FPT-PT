package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


@Autonomous
@Configurable // Panels
public class Auto extends OpMode {

    public enum PathState {
        PICK_UP1,
        SHOOT,
        PICK_UP2,
        LOW_ZONE,
        HIGH_ZONE,
        LEAVE,
    }
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private PathState pathState, nextPathState;
    private ShooterLogic shooter;

    public PathChain pickup1, lowScore, pickup2, highScore;
    public Pose lowZonePose = new Pose(81.115, 5.735);
    public Pose pickup1Pose = new Pose(121.468, 36.871);
    public Pose pickup2Pose = new Pose(119.215, 82.959);
    public Pose highZonePose = new Pose(77.838, 77.838);


    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();
        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(80, 16, Math.toRadians(90)));
        buildPath(follower);

        shooter.init(hardwareMap);
        setPathState(PathState.PICK_UP1);
    }

    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        shooter.update(); // Update shooter logic

        pathState = autonomousPathUpdate(); // Update autonomous state machine

        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.update(telemetry);
    }

    public void buildPath(Follower follower) {
        pickup1 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(81.115, 5.735), new Pose(90.000, 35.000), new Pose(121.468, 36.871))
                )
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(0))
                .build();

        lowScore = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(121.468, 36.871), new Pose(81.115, 5.735))
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(120))
                .build();

        pickup2 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(new Pose(81.115, 5.735),new Pose(90.000, 60.000),new Pose(121.468, 60.427))
                )
                .setLinearHeadingInterpolation(Math.toRadians(120), Math.toRadians(0))
                .build();

        highScore = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(121.468, 60.427), new Pose(80.706, 5.735))
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(135))
                .build();

    }

    boolean  shotTriggered = false;
    private PathState autonomousPathUpdate() {
        switch (pathState) {
            case SHOOT:
                if(follower.isBusy()) break;

                if(!shotTriggered) {
                    shooter.fireShots(3, true);
                    shotTriggered = true;
                } else if(!shooter.isBusy()) {
                    shotTriggered = false;
                    setPathState(nextPathState);
                }
                break;
            case PICK_UP1:
                follower.followPath(pickup1);
                setPathState(PathState.LOW_ZONE);
                break;
            case LOW_ZONE:
                if(!follower.isBusy()) {
                    follower.followPath(lowScore);
                    setPathState(PathState.SHOOT);
                    nextPathState = PathState.PICK_UP2;
                }
                break;
            case PICK_UP2:
                follower.followPath(pickup2);
                setPathState(PathState.HIGH_ZONE);
                break;
            case HIGH_ZONE:
                if(!follower.isBusy()) {
                    follower.followPath(highScore);
                    setPathState(PathState.SHOOT);
                    nextPathState = PathState.LEAVE;
                }
                break;
            case LEAVE:
                panelsTelemetry.addData("done","leave");
                break;
        }
        return pathState;
    }

    public void setPathState(PathState pState) {
        pathState = pState;
    }
}