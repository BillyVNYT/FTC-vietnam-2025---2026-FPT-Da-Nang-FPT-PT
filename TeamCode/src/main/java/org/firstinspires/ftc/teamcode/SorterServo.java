package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.ColorSensor;

@TeleOp
public class SorterServo extends LinearOpMode {
    Servo servo;
    DcMotor motor;
    ColorSensor colorSensor;

    int idx = 0;

    double[] positions = {0.385, 0.455, 0.52};
    boolean enough = false;

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.servo.get("s1");
        motor = hardwareMap.dcMotor.get("m1");
        colorSensor = hardwareMap.get(ColorSensor.class, "tcs_sensor");

        servo.setPosition(positions[0]);
        motor.setPower(1);
        waitForStart();

        while (opModeIsActive()) {
            String color = colorSensor.detectBallColor();
            if(!color.equals("NONE")) {
                if(idx < positions.length-1) {
                    idx++;
                } else {
                    motor.setPower(0);
                    enough = true;
                }
            }

            double position = positions[idx%positions.length];
            if(enough) position = 0;
            servo.setPosition(position);
            sleep(500);

            telemetry.addData("Servo pos: ", position);
            telemetry.addData("Color:", color);

            telemetry.addData("Raw R", colorSensor.getRed());
            telemetry.addData("Raw G", colorSensor.getGreen());
            telemetry.addData("Raw B", colorSensor.getBlue());
            telemetry.update();
        }
    }
}