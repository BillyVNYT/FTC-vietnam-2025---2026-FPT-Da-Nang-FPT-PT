package org.firstinspires.ftc.teamcode.Autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
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
@Autonomous(name="AutoRed2", group = "Auto")
public class AutoRed2 extends LinearOpMode {
    public IntakeFPT2 intake;
    public ShooterFPT2 shooter;
    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d beginPose = new Pose2d(64, 8, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);
        intake = new IntakeFPT2(hardwareMap);
        shooter = new ShooterFPT2(hardwareMap, intake);
        shooter.SAngle.setPosition(1);
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
        shooter.setMotorVelocity(2200);
        Actions.runBlocking(drive.actionBuilder(beginPose)
                .strafeToLinearHeading(new Vector2d(54, 18), Math.toRadians(155))
                .waitSeconds(3)
                .afterTime(0, ShootBall())
                .waitSeconds(2)
                .afterTime(0, CloseShootBall())
                .splineToLinearHeading(new Pose2d(63, 35, Math.toRadians(-270)), Math.toRadians(90))
                .lineToY(63, new TranslationalVelConstraint(45))
                .lineToY(35)
                .lineToY(63, new TranslationalVelConstraint(45))
                .lineToY(35)
                .lineToY(63, new TranslationalVelConstraint(45))
                .lineToY(35)
                .splineToLinearHeading(
                        new Pose2d(new Vector2d(53, 18), Math.toRadians(135)),
                        Math.toRadians(90)
                )
                .waitSeconds(5)
                .afterTime(0, ShootBall())
                .waitSeconds(3)
                .afterTime(0, CloseShootBall())
                .build());
    }
    public class shootBall implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shooter.SAngle.setPosition(1);
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
            shooter.setMotorVelocity(2350);
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
}
