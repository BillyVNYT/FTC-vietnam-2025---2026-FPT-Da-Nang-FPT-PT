package org.firstinspires.ftc.teamcode.Autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.utils.IntakeFPT2;
import org.firstinspires.ftc.teamcode.utils.ShooterFPT2;
//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
@Autonomous(name="AutoRed1", group = "Auto")
public class AutoRed1 extends LinearOpMode {
    public IntakeFPT2 intake;
    public ShooterFPT2 shooter;
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(-54, 44, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);
        intake = new IntakeFPT2(hardwareMap);
        shooter = new ShooterFPT2(hardwareMap, intake);
        shooter.SAngle.setPosition(0.55);
        intake.HoldBall = true;
        intake.checkHoldBall();
        shooter.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_OCEAN_PALETTE);
        Thread runOther = new Thread(() -> {
            while (opModeIsActive()) {
                shooter.holdMotor(shooter.MTurnOuttake);
            }
        });
        runOther.start();
        waitForStart();
        if(isStopRequested()) return;
        shooter.setMotorVelocity(1450);
        Actions.runBlocking(drive.actionBuilder(beginPose)
                        .strafeToLinearHeading(new Vector2d(-23, 23), Math.toRadians(135))
                        .afterTime(0, ShootBall())
                        .waitSeconds(1.5)
                        .afterTime(0, CloseShootBall2())
                        .splineToLinearHeading(new Pose2d(14, 18, Math.toRadians(90)), Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(14, 59), Math.toRadians(80), new TranslationalVelConstraint(30))
                        .afterTime(0, StopIntake())
                        .splineToConstantHeading(new Vector2d(12, 45), Math.toRadians(90), new TranslationalVelConstraint(20))
                        .splineToConstantHeading(new Vector2d(5, 55), Math.toRadians(90))
                        .waitSeconds(1)
                        .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.toRadians(180))
                        .waitSeconds(0.5)
                        .afterTime(0.5, ShootBall())
                        .waitSeconds(1.5)
                        .afterTime(0, CloseShootBall())
                        .splineToLinearHeading(new Pose2d(-13, 35, Math.toRadians(90)), Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(-13, 55), Math.toRadians(90), new TranslationalVelConstraint(30))
                        .afterTime(2, StopIntake())
                        .splineToLinearHeading(new Pose2d(-23, 23, Math.toRadians(135)), Math.toRadians(90))
                        .afterTime(0, ShootBall())
                        .waitSeconds(1.5)
                        .afterTime(0, CloseShootBall())
                        .splineToLinearHeading(new Pose2d(38,32, Math.toRadians(90)),Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(38, 65), Math.toRadians(90), new TranslationalVelConstraint(30))
                        .afterTime(0.5, StopIntake())
                        .splineToLinearHeading(new Pose2d(-23, 23, Math.toRadians(135)), Math.toRadians(180))
                        .afterTime(0, ShootBall())
                        .waitSeconds(1.5)
                        .afterTime(0, CloseShootBall())
//                        .splineToLinearHeading(new Pose2d(9, 60, Math.toRadians(135)), Math.toRadians(90))
//                        .strafeTo(new Vector2d(9, 58))
//                        .splineToLinearHeading(new Pose2d(-13, 13, Math.toRadians(135)), Math.toRadians(270))
                .build());
    }
    public class shootBall implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shooter.SAngle.setPosition(0.525);
            intake.HoldBall = false;
            intake.checkHoldBall();
            intake.isActive();
            return false;
        }
    }
    public Action ShootBall(){
        return new shootBall();
    }

    public class closeShootBall implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shooter.setMotorVelocity(1500);
            intake.HoldBall = true;
            intake.checkHoldBall();
            intake.intake.setPower(1);
            return false;
        }
    }
    public Action CloseShootBall(){
        return new closeShootBall();
    }
    public class stopIntake implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intake.stop();
            return false;
        }
    }
    public Action StopIntake(){
        return new stopIntake();
    }
    public class closeShootBall2 implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shooter.setMotorVelocity(1650);
            intake.HoldBall = true;
            intake.checkHoldBall();
            intake.intake.setPower(1);
            return false;
        }
    }
    public Action CloseShootBall2(){
        return new closeShootBall();
    }
}
