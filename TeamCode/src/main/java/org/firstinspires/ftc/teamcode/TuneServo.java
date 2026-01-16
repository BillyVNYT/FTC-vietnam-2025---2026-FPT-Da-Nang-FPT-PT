package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TuneServo extends LinearOpMode {
    Servo servo;
    double pos;
    boolean reversed = false;
    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.servo.get("s4");
        servo.setPosition(0);
        waitForStart();

        while (opModeIsActive()) {
//            if (gamepad1.right_bumper && pos < 1) {
//                pos += 0.0002;
//                servo.setPosition(pos);
//            }
//
//            if (gamepad1.left_bumper && pos > 0) {
//                pos -= 0.0002;
//                servo.setPosition(pos);
//            }
//
//            if (gamepad1.crossWasPressed()) {
//                servo.setDirection(reversed ? Servo.Direction.FORWARD : Servo.Direction.REVERSE);
//                reversed = !reversed;
//            }
//            servo.setPosition(0);
//            sleep(5000);
//            telemetry.addData("Position", servo.getPosition());
//            telemetry.update();
//            servo.setPosition(1);
//            sleep(5000);
//            telemetry.addData("Position", servo.getPosition());
            servo.setPosition(0.5);
            telemetry.update();
        }
    }
}
