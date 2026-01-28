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
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    MainRobot(int goalId, HardwareMap hardwareMap, Gamepad gamepad2) {
        driveTrain = new DriveTrain(hardwareMap);
        manualControl2 = new ManualControl2(hardwareMap, gamepad2);
        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        s4 = hardwareMap.get(Servo.class,"s4");
        s4.setPosition(0.36);
        this.goalId = goalId;
        this.gamepad2 = gamepad2;
    }

    // --- Bật tất cả các Thread phụ lên ---
    public void startThreads() {
        if (isRunning.get()) return;
        isRunning.set(true);

        // 1. Thread cho Drivetrain (Lái xe riêng cho mượt)
        driveThread = new Thread(() -> {
            while (isRunning.get() && !Thread.currentThread().isInterrupted()) {
                driveTrain.drivetrainControlBasic(gamepad2);
                // Lái xe cần tốc độ cao, ko nên sleep lâu
            }
        });

        // 2. Thread cho HoldShooter (Quét AprilTag và chạy PID liên tục)
        holdShooterThread = new Thread(() -> {
            while (isRunning.get() && !Thread.currentThread().isInterrupted()) {
                // Tách riêng PID xoay shooter ra đây để nó bám mục tiêu liên tục
                // Truyền null vào telemetry để tránh xung đột thread khi ghi log
                manualControl2.holdShooter(goalId, null);
                try {
                    Thread.sleep(10); // Nghỉ 10ms để đỡ tốn CPU
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        driveThread.start();
        holdShooterThread.start();
    }

    // --- Tắt tất cả Thread ---
    public void stopThreads() {
        isRunning.set(false);
        try {
            if (driveThread != null) driveThread.join(200);
            if (holdShooterThread != null) holdShooterThread.join(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void manageShootBallThread(Telemetry telemetry) throws InterruptedException {
        manualControl2.shootBall(telemetry);
    }

    // --- Vòng lặp chính (Main Thread) ---
    public void opMode(Telemetry telemetry) throws InterruptedException {
        // Chỉ để lại những cái xử lý nút nhấn và logic nhẹ
        manualControl2.updateShooterAngleServo(telemetry);

        // Toggle Flywheel để ở đây là chuẩn nhất vì nó bắt sự kiện nhấn nút
        manualControl2.toggleFlywheel(telemetry);

        manualControl2.controlIntakeShaft(telemetry);
        manualControl2.updateIntakeReverse();

        // holdShooter đã được ném sang Thread riêng, xóa ở đây cho rảnh máy
    }
}