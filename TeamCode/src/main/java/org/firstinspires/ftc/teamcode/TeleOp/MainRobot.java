package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ManualControl2;
import org.firstinspires.ftc.teamcode.utils.DriveTrain;

public class MainRobot {
    private DriveTrain driveTrain;
    private ManualControl2 manualControl2;
    int goalId;
    Gamepad gamepad2;

    MainRobot(int goalId, HardwareMap hardwareMap, Gamepad gamepad2) {
        driveTrain = new DriveTrain(hardwareMap);
        manualControl2 = new ManualControl2(hardwareMap, gamepad2);
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
                    manualControl2.shootBall(telemetry);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        shooterThread.start();
    }

    public void opMode(Telemetry telemetry) throws InterruptedException {
        driveTrain.drivetrainControlBasic(gamepad2);
        manualControl2.updateShooterAngleServo(telemetry);
        manualControl2.toggleFlywheel(telemetry);
        manualControl2.controlIntakeShaft(telemetry);

        manualControl2.updateIntakeReverse();
//        manualControl2.shootPurpleBall(telemetry);
//        manualControl2.shootGreenBall(telemetry);
        manualControl2.holdShooter(goalId, telemetry);

//        manualControl2.tuneP(telemetry);
//        manualControl2.updateTuneFactor(telemetry);
//        manualControl2.updateTuneStep(telemetry);
//        manualControl2.controlTpr(telemetry);
    }
}
