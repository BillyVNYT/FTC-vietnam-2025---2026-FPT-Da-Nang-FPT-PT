package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TestServos extends LinearOpMode {
    Servo ServoIntakeLeft, ServoIntakeRight;
    Servo ServoKepIntakeLeft, ServoKepIntakeRight;
    Servo ServoOuttake, ServoKepOuttake;
    int testingServo = 0;
    double servoPos = 0;
    Servo.Direction servoDirection = Servo.Direction.FORWARD;
    boolean isChangingServo = false;

    void resetServoValues() {
        if(testingServo == 2) {
            servoPos = 0.8;
        } else if(testingServo ==3) {
            servoPos = 0.3;
        } else {
            servoPos = 0;
        }

        servoDirection = Servo.Direction.FORWARD;
    }

    void changeTestingServo(int number) {
        if(isChangingServo) return;

        isChangingServo = true;
        testingServo += number;
        resetServoValues();
        updateServoPos();
        updateServoDirection();
        sleep(1000);
        isChangingServo = false;
    }

    void updateServoPos() {
        switch (testingServo) {
            case 0:
                ServoIntakeRight.setPosition(servoPos);
                break;
            case 1:
                ServoIntakeLeft.setPosition(servoPos);
                break;
            case 2:
                ServoOuttake.setPosition(servoPos);
                break;
            case 3:
                ServoKepOuttake.setPosition(servoPos);
                break;
            case 4:
                ServoKepIntakeLeft.setPosition(servoPos);
                break;
            case 5:
                ServoKepIntakeRight.setPosition(servoPos);
                break;

        }
    }

    void updateServoDirection() {
        switch (testingServo) {
            case 0:
                ServoIntakeRight.setDirection(servoDirection);
                break;
            case 1:
                ServoIntakeLeft.setDirection(servoDirection);
                break;
            case 2:
                ServoOuttake.setDirection(servoDirection);
                break;
            case 3:
                ServoKepOuttake.setDirection(servoDirection);
                break;
            case 4:
                ServoKepIntakeLeft.setDirection(servoDirection);
                break;
            case 5:
                ServoKepIntakeRight.setDirection(servoDirection);
                break;

        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        ServoIntakeLeft = hardwareMap.servo.get("ServoIntakeLeft");
        ServoIntakeRight = hardwareMap.servo.get("ServoIntakeRight");
        ServoOuttake = hardwareMap.servo.get("ServoOuttake");
        ServoKepOuttake = hardwareMap.servo.get("ServoKepOuttake");
        ServoKepIntakeLeft = hardwareMap.servo.get("ServoKepIntakeLeft");
        ServoKepIntakeRight = hardwareMap.servo.get("ServoKepIntakeRight");

        telemetry.addData(">", "Init Successfully");
        telemetry.update();
        waitForStart();

        if (isStopRequested()) return;

        while(opModeIsActive()) {
            if(gamepad1.dpad_up) {
                changeTestingServo(1);
            } else if (gamepad1.dpad_down) {
                changeTestingServo(-1);
            }

            if(gamepad1.a && !isChangingServo) {
                isChangingServo = true;
                if(servoDirection == Servo.Direction.REVERSE) {
                    servoDirection = Servo.Direction.FORWARD;
                } else {
                    servoDirection = Servo.Direction.REVERSE;
                }
                updateServoDirection();

                sleep(500);
                isChangingServo = false;
            }

            if(gamepad1.right_bumper && servoPos < 1) {
                servoPos += 0.0005;
                updateServoPos();
            }
            if(gamepad1.left_bumper && servoPos > 0){
                servoPos -= 0.0005;
                updateServoPos();
            }

            telemetry.addData("Servo pos: ", servoPos);
            telemetry.addData("Servo direction: ", servoDirection);
            telemetry.addData("Testing servo: ", testingServo);
            telemetry.addData("Is changing servo: ", isChangingServo);
            telemetry.update();
        }
    }
}
