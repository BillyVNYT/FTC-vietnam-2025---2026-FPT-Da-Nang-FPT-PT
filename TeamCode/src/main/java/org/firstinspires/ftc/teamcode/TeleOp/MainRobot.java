package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ManualControl2;
import org.firstinspires.ftc.teamcode.utils.DriveTrain;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainRobot {
    private final DriveTrain driveTrain;
    private final ManualControl2 manualControl2;
    int goalId;
    Gamepad gamepad2;
    Servo s4;

    // Quản lý Thread
    private Thread driveThread;
    private Thread holdShooterThread;
    private Thread shooterThread;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    MainRobot(int goalId, HardwareMap hardwareMap, Gamepad gamepad2, Telemetry telemetry) {
        driveTrain = new DriveTrain(hardwareMap, goalId == 24);
        manualControl2 = new ManualControl2(hardwareMap, gamepad2, telemetry, goalId == 24);
        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        s4 = hardwareMap.get(Servo.class,"s4");
        s4.setPosition(0.36);
        this.goalId = goalId;
        this.gamepad2 = gamepad2;
    }

    public void startThreads() {
        if (isRunning.get()) return;
        isRunning.set(true);

        // 1. Thread cho Drivetrain
        driveThread = new Thread(() -> {
            try {
                while (isRunning.get() && !Thread.currentThread().isInterrupted()) {
                    driveTrain.drivetrainControlBasic(gamepad2);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // 2. Thread cho HoldShooter
        holdShooterThread = new Thread(() -> {
            try {
                while (isRunning.get() && !Thread.currentThread().isInterrupted()) {
                    manualControl2.holdShooter(goalId);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });

        shooterThread = new Thread(() -> {
            try {
                while (isRunning.get() && !Thread.currentThread().isInterrupted()) {
                    manualControl2.shootBall();
//                    manualControl2.shootSorted();
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        holdShooterThread.start();
        driveThread.start();
        shooterThread.start();
    }

    public void stopThreads() {
        isRunning.set(false);
        try {
            if (driveThread != null) {
                driveThread.join();
                driveThread = null;
            }
            if (holdShooterThread != null) {
                holdShooterThread.join();
                holdShooterThread = null;
            }
            if (shooterThread != null) {
                shooterThread.join();
                shooterThread = null;
            }

            manualControl2.stopThreadShakeServo();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void opMode(Telemetry telemetry) throws InterruptedException {
        manualControl2.updateShooterAngleServo();
        manualControl2.toggleFlywheel();
        manualControl2.controlIntakeShaft();
        manualControl2.printShooter();

        manualControl2.updateMTurnOuttakeAngle();
        manualControl2.holdShooter(goalId);
        manualControl2.updateIntakeReverse();

        driveTrain.logDriveTrain(telemetry);


    }
}