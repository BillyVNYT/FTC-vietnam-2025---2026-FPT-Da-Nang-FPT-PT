package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.utils.ColorSensor;
import org.firstinspires.ftc.teamcode.utils.SortBall;

@TeleOp(name="Auto Ball Sorter - Full Mode", group="Production")
public class Tuneclss extends LinearOpMode {

    // Khai báo 2 con cảm biến cho 1 cặp
    private ColorSensor sensor1;
    private ColorSensor sensor2;

    @Override
    public void runOpMode() {
        // 1. Khởi tạo Hardware - Nhớ đặt tên chuẩn trong Robot Config trên Driver Station
        sensor1 = hardwareMap.get(ColorSensor.class, "cs3");
        sensor2 = hardwareMap.get(ColorSensor.class, "cs4");

        telemetry.addLine("Đã sẵn sàng. Đợi mày bấm Play đó tml...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // 2. Gọi hàm nhận diện "thông minh"
            SortBall.BallColor detectedColor = detectLogic();

            // 3. Xử lý kết quả nhận diện
            switch (detectedColor) {
                case GREEN:
                    telemetry.addData("KẾT QUẢ", "Bóng XANH LÁ - Đang xử lý...");
                    // Thêm code điều khiển motor/servo để gạt bóng xanh ở đây
                    break;

                case PURPLE:
                    telemetry.addData("KẾT QUẢ", "Bóng TÍM - Đang xử lý...");
                    // Thêm code điều khiển motor/servo để gạt bóng tím ở đây
                    break;

                case EMPTY:
                    telemetry.addData("KẾT QUẢ", "Trống không (Éo có bóng)");
                    break;
            }

            telemetry.addLine("----------------------------");
            telemetry.update();

            // Nghỉ một tí cho đỡ nóng máy, 20ms là đủ
            sleep(20);
        }
    }

    /**
     * Logic nhận diện dựa trên thông số thực tế mày đo được.
     * Thằng nào Clear cao hơn = Thằng đó đang gần bóng hơn.
     */
    private SortBall.BallColor detectLogic() {
        int c1 = sensor1.getClear();
        int c2 = sensor2.getClear();

        // Chọn con cảm biến đang "nhìn" thấy vật thể rõ nhất
        ColorSensor activeSensor = (c1 > c2) ? sensor1 : sensor2;
        int activeC = Math.max(c1, c2);

        // Ngưỡng Clear tối thiểu để xác nhận có vật thể (Dựa trên mức 2816 mày đo)
        if (activeC < 1800) {
            return SortBall.BallColor.EMPTY;
        }

        // Tính toán tỉ lệ của con đang hoạt động
        double rRatio = (double) activeSensor.getRed() / activeC;
        double gRatio = (double) activeSensor.getGreen() / activeC;

        // Dùng hiệu số (G - R) để phân loại
        double diff = gRatio - rRatio;

        telemetry.addData("Cảm biến chính", (c1 > c2) ? "Số 1" : "Số 2");
        telemetry.addData("Clear Value", activeC);
        telemetry.addData("Diff (G-R)", "%.5f", diff);
        telemetry.addData("G", "%.5f", gRatio);
        telemetry.addData("R", "%.5f", rRatio);
        telemetry.addData("Activesensor", activeSensor);

        /* PHÂN TÍCH DATA CỦA MÀY:
           - Bóng Xanh: diff = 0.27 đến 0.37
           - Bóng Tím: diff = -0.05 đến 0.06
           => Lấy ngưỡng 0.15 làm vạch kẻ đường chia đôi cuộc tình.
        */

        if (diff > 0.15) {
            return SortBall.BallColor.GREEN;
        } else {
            // Vì đã qua check Clear ở trên, nên nếu không phải xanh thì là tím
            // (Thậm chí diff có là 0.06 như con sensor 2 của mày vẫn ăn)
            return SortBall.BallColor.PURPLE;
        }
    }
}
