package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.DriveTrain;
import org.firstinspires.ftc.teamcode.ManualControl2;

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
    private DriveTrain driveTrain;
    private ManualControl2 manualControl2;

    @Override
    public void runOpMode() throws InterruptedException {
        driveTrain = new DriveTrain(hardwareMap);
        manualControl2 = new ManualControl2(hardwareMap, gamepad2);

        waitForStart();
        while (opModeIsActive()){
//            driveTrain.drivetrainControlAdvanced(gamepad1);
            driveTrain.drivetrainControlBasic(gamepad2);
//
//            manualControl2.controlTurnOutTake(telemetry);
            manualControl2.updateShooterAngleServo(telemetry);
            manualControl2.toggleFlywheel();

            manualControl2.shootBall(telemetry);
            manualControl2.controlIntakeShaft(telemetry);

            manualControl2.shootPurpleBall(telemetry);
            manualControl2.shootGreenBall(telemetry);
        }
    }
}
