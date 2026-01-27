package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ManualControl;
import org.firstinspires.ftc.teamcode.utils.DriveTrain;
import org.firstinspires.ftc.teamcode.utils.DriveTrainFPT2;

public class SubRobot {
    private final DriveTrainFPT2 driveTrain;
    private final ManualControl manualControl;
    int goalId;
    Gamepad gamepad2;

    public SubRobot(int goalId, HardwareMap hardwareMap, Gamepad gamepad2) {
        driveTrain = new DriveTrainFPT2(hardwareMap);
        manualControl = new ManualControl(hardwareMap, gamepad2);

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        this.goalId = goalId;
        this.gamepad2 = gamepad2;
    }

    public void manageShootBallThread(Telemetry telemetry) {
        Thread shooterThread = new Thread(() -> {
            try {
                while (true) {
                    manualControl.shootBall(telemetry);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        shooterThread.start();
    }

    public void opMode(Telemetry telemetry) throws InterruptedException {
        driveTrain.drivetrainControlBasic(gamepad2);
        manualControl.updateShooterAngleServo(telemetry);
//        manualControl.toggleFlywheel();
        manualControl.shootBall(telemetry);
        manualControl.toggleIntake();
//
        manualControl.holdShooter(goalId, telemetry, false);
//        manualControl.checkTunnelFull();
    }
}
