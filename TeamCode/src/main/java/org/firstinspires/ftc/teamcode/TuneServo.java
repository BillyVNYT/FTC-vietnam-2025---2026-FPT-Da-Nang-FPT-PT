package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TuneServo extends LinearOpMode {
    Servo servo;
    double pos;
    boolean reversed = false;
    boolean auto = false;

    private void printDescription() {
        telemetry.addLine("Press X to reverse direction");
        telemetry.addLine("Press Square to toggle auto mode");
        if (!auto) {
            telemetry.addLine("Press Right Bumper to increase position");
            telemetry.addLine("Press Left Bumper to decrease position");
            telemetry.addData("pos", pos);
        }
        telemetry.update();
    }

    private void tunePosition() {
        if (gamepad1.right_bumper && pos < 1) {
            pos += 0.0002;
            servo.setPosition(pos);
        }
        if (gamepad1.left_bumper && pos > 0) {
            pos -= 0.0002;
            servo.setPosition(pos);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.servo.get("s1");
        servo.setPosition(0);
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.squareWasPressed()) {
                auto = !auto;
            }

            if(auto) {
                servo.setPosition(0);
                telemetry.addData("Position", servo.getPosition());
                telemetry.update();
                sleep(5000);
                servo.setPosition(1);
                telemetry.addData("Position", servo.getPosition());
                telemetry.update();
                sleep(5000);
            } else tunePosition();

            if (gamepad1.crossWasPressed()) {
                servo.setDirection(reversed ? Servo.Direction.FORWARD : Servo.Direction.REVERSE);
                reversed = !reversed;
            }

            printDescription();
        }
    }
}
