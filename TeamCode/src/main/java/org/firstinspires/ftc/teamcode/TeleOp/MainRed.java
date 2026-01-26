package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.drawOnlyCurrent;
import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.pedropathing.Drivetrain;
import org.firstinspires.ftc.teamcode.utils.DriveTrain;
import org.firstinspires.ftc.teamcode.ManualControl2;
import org.firstinspires.ftc.teamcode.utils.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Tuning;
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
@TeleOp
public class MainRed extends LinearOpMode {
    Drivetrain drivetrain;
    @Override
    public void runOpMode() throws InterruptedException {
        MainRobot robot = new MainRobot(24, hardwareMap, gamepad1);

        follower.setStartingPose(new Pose(72,72));
        follower.update();
        drawOnlyCurrent();
        waitForStart();
        follower.startTeleopDrive();
        follower.update();
        while (opModeIsActive()){
            follower.update();
            robot.opMode(telemetry);
        }
    }
}
