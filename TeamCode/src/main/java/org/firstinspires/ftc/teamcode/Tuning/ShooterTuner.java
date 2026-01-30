package org.firstinspires.ftc.teamcode.Tuning; // Nhớ đổi đúng package của mày

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.utils.Shooter;

@TeleOp(name="PIDF_Tuner_Final", group="Tuning")
public class ShooterTuner extends LinearOpMode {

    Shooter shooter;
    double targetVelocity = 1500;
    String[] params = {"P", "I", "D", "F", "Target"};
    int currentParamIdx = 0;
    boolean motorOn = false;

    // Biến để chặn việc nhấn giữ nút bị nhảy số quá nhanh
    boolean lastUp = false, lastDown = false, lastA = false;

    @Override
    public void runOpMode() throws InterruptedException {
        // Khởi tạo shooter
        shooter = new Shooter(hardwareMap, false, telemetry);

        telemetry.addLine("--- CHƯƠNG TRÌNH TUNE SHOOTER ---");
        telemetry.addLine("Dpad UP/DOWN: Chọn thông số (P, I, D, F, Target)");
        telemetry.addLine("Dpad LEFT/RIGHT: Tăng/Giảm giá trị");
        telemetry.addLine("Nút A: Bật/Tắt Motor");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // 1. Logic chọn thông số (Up/Down)
            if (gamepad1.dpad_up && !lastUp) {
                currentParamIdx = (currentParamIdx - 1 + params.length) % params.length;
            }
            if (gamepad1.dpad_down && !lastDown) {
                currentParamIdx = (currentParamIdx + 1) % params.length;
            }
            lastUp = gamepad1.dpad_up;
            lastDown = gamepad1.dpad_down;

            // 2. Logic tăng giảm giá trị (Left/Right)
            double step = 0;
            if (gamepad1.dpad_right) step = getStep(params[currentParamIdx]);
            if (gamepad1.dpad_left) step = -getStep(params[currentParamIdx]);

            if (step != 0) {
                updateValue(params[currentParamIdx], step);
                // Nếu đang chỉnh P, I, D, hoặc F thì phải nạp lại vào Motor
                if (currentParamIdx < 4) {
                    shooter.tunePidf(0, telemetry); // Gọi hàm này để nó setPIDFCoefficients lại
                }
                sleep(150); // Delay nhẹ để không bị nhảy số quá gắt
            }

            // 3. Bật/Tắt motor (Nút A)
            if (gamepad1.a && !lastA) {
                motorOn = !motorOn;
            }
            lastA = gamepad1.a;

            if (motorOn) {
                shooter.setMotorVelocity((int)targetVelocity);
            } else {
                shooter.setMotorVelocity(0);
            }

            // 4. HIỂN THỊ LÊN MÀN HÌNH (Đây là thứ mày cần)
            telemetry.addLine("== ĐANG CHỈNH: " + params[currentParamIdx] + " ==");
            telemetry.addData("Motor State", motorOn ? "ĐANG CHẠY" : "DỪNG");
            telemetry.addLine("--------------------------");
            // Mày phải để P, I, D, F trong class Shooter là public thì dòng dưới mới chạy nhé
            telemetry.addData("P", "%.4f", shooter.P);
            telemetry.addData("I", "%.4f", shooter.I);
            telemetry.addData("D", "%.4f", shooter.D);
            telemetry.addData("F", "%.4f", shooter.F);
            telemetry.addData("Target Velocity", targetVelocity);
            telemetry.addLine("--------------------------");

            double[] currentVels = shooter.getVelocity();
            telemetry.addData("Vận tốc thực M1", currentVels[0]);
            telemetry.addData("Vận tốc thực M2", currentVels[1]);
            telemetry.addData("Sai số (Error)", targetVelocity - currentVels[0]);

            telemetry.update();
        }
    }

    // Hàm lấy bước nhảy cho từng loại thông số
    private double getStep(String param) {
        switch (param) {
            case "P": return 0.1;
            case "I": return 0.0001;
            case "D": return 0.01;
            case "F": return 0.05;
            case "Target": return 50;
            default: return 0;
        }
    }

    // Hàm cập nhật giá trị trực tiếp vào class Shooter
    private void updateValue(String param, double step) {
        switch (param) {
            case "P": shooter.P += step; break;
            case "I": shooter.I += step; break;
            case "D": shooter.D += step; break;
            case "F": shooter.F += step; break;
            case "Target": targetVelocity += step; break;
        }
    }
}