package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.AutoTrackStartMatch;
import org.firstinspires.ftc.teamcode.DriveTrain;
import org.firstinspires.ftc.teamcode.Intake;
import org.firstinspires.ftc.teamcode.ManualControl;
import org.firstinspires.ftc.teamcode.utils.Shooter;

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
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
public class MainRed extends LinearOpMode {
    private DriveTrain driveTrain = new DriveTrain(hardwareMap);
    private Shooter outtake = new Shooter(hardwareMap);
    private ManualControl manualControl = new ManualControl(hardwareMap, gamepad1, gamepad2);
    private Intake intake = new Intake(hardwareMap, gamepad1, gamepad2);
    private AutoTrackStartMatch autoTrackStartMatch = new AutoTrackStartMatch(hardwareMap);
    private boolean AutoHold = true;
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        intake.Start();
        while (opModeIsActive()){
            driveTrain.DrivetrainControlAdvanced(gamepad1, gamepad2);
            if(gamepad1.right_bumper){
                outtake.shoot(telemetry);
            }
            if(gamepad1.start){
                AutoHold = true;
            }
            if(AutoHold){
                outtake.trackAprilTag(telemetry);
            } else {
                manualControl.ControlTurnOutTake();
            }
            manualControl.ControlIntakeShaft();
            intake.CheckCommandControl();
        }
    }
}
