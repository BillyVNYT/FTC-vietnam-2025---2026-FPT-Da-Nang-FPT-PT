package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Test TCS34725")
public class TestTCS34725 extends LinearOpMode {
    TCS34725Driver colorSensor;

    @Override
    public void runOpMode() {
        colorSensor = hardwareMap.get(TCS34725Driver.class, "tcs_sensor");

        waitForStart();

        while (opModeIsActive()) {
            String color = colorSensor.detectBallColor();

            if (color.equals("GREEN")) {
                telemetry.addData("Result:", "BONG XANH");
            } else if (color.equals("PURPLE")) {
                telemetry.addData("Result:", "BONG TIM  ");
            } else {
                telemetry.addData("Result:", "KHONG THAY BONG");
            }

            // In ra để debug nếu cần
            telemetry.addData("Raw R", colorSensor.getRed());
            telemetry.addData("Raw G", colorSensor.getGreen());
            telemetry.addData("Raw B", colorSensor.getBlue());
            telemetry.update();
        }
    }
}