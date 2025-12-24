package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ganhTem_assistCountry extends LinearOpMode{
    DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;

    Servo ServoIntakeLeft, ServoIntakeRight;
    Servo ServoKepIntakeLeft, ServoKepIntakeRight;
    Servo ServoOuttake, ServoKepOuttake;

    double KepIntakeLeftClosePos = 0.74;

    double KepIntakeRightClosePos = 0.25;
    double KepIntakeLeftOpenPos = 0.54;
    double KepIntakeRightOpenPos = 0.16;
    double KepOuttakeClosePos = 0.38;
    double KepOuttakeOpenPos = 0.03;

    double OuttakeInitPos = 0.79;
    double IntakeInitPos = 0;

    double ServoIntakePos = IntakeInitPos;
    double ServoOuttakePos = OuttakeInitPos;

    double OuttakeGrabPos = 0.84;
    double OuttakeHighPos = 0.55;

    double IntakeLowPos = 0.22;
    double IntakeMidPos = 0.36;
    final double speed = 0.6;

    boolean intakeBusy = false;
    boolean outtakeBusy = false;

    void grabIntake() {
        if(!intakeBusy) {
            ServoKepIntakeLeft.setPosition(KepIntakeLeftClosePos);
            ServoKepIntakeRight.setPosition(KepIntakeRightClosePos);
            intakeBusy = true;
        }
    }

    void releaseIntake() {
        if(intakeBusy) {
            ServoKepIntakeLeft.setPosition(KepIntakeLeftOpenPos);
            ServoKepIntakeRight.setPosition(KepIntakeRightOpenPos);
            intakeBusy = false;
        }
    }

    void grabOuttake() {
        if(!outtakeBusy) {
            ServoKepOuttake.setPosition(KepOuttakeClosePos);
            sleep(400);
            ServoOuttakePos = OuttakeInitPos;
            ServoOuttake.setPosition(ServoOuttakePos);
            outtakeBusy = true;
        }
    }

    void releaseOuttake() {
        if(outtakeBusy) {
            ServoKepOuttake.setPosition(KepOuttakeOpenPos);
            outtakeBusy = false;
        }
    }

    void controlIntakeServos() {
        if(gamepad1.right_trigger > 0 && ServoIntakePos < 1) {
            ServoIntakePos += 0.002;
            ServoIntakeLeft.setPosition(ServoIntakePos);
            ServoIntakeRight.setPosition(ServoIntakePos);
        }

        if (gamepad1.left_trigger > 0 && ServoIntakePos > 0) {
            ServoIntakePos -= 0.002;
            ServoIntakeLeft.setPosition(ServoIntakePos);
            ServoIntakeRight.setPosition(ServoIntakePos);
        }
    }

    void controlOuttakeServo () {
        if(gamepad1.dpad_down && ServoOuttakePos < OuttakeGrabPos) {
            ServoOuttakePos += 0.0005;
            ServoOuttake.setPosition(ServoOuttakePos);
        }

        if (gamepad1.dpad_up && ServoOuttakePos > OuttakeHighPos) {
            ServoOuttakePos -= 0.0005;
            ServoOuttake.setPosition(ServoOuttakePos);
        }
    }

    void resetIntake() {
        if(!intakeBusy) {
            ServoIntakePos = IntakeInitPos;
            ServoIntakeLeft.setPosition(ServoIntakePos);
            ServoIntakeRight.setPosition(ServoIntakePos);
        }
    }

    void resetOuttake() {
      if(!outtakeBusy) {
          ServoOuttakePos = OuttakeInitPos;
          ServoOuttake.setPosition(ServoOuttakePos);
      }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        frontLeftMotor = hardwareMap.dcMotor.get("leftFront");
        backLeftMotor = hardwareMap.dcMotor.get("leftBack");
        frontRightMotor = hardwareMap.dcMotor.get("rightFront");
        backRightMotor = hardwareMap.dcMotor.get("rightBack");

        ServoIntakeLeft = hardwareMap.servo.get("ServoIntakeLeft");
        ServoIntakeRight = hardwareMap.servo.get("ServoIntakeRight");
        ServoOuttake = hardwareMap.servo.get("ServoOuttake");
        ServoKepOuttake = hardwareMap.servo.get("ServoKepOuttake");
        ServoKepIntakeLeft = hardwareMap.servo.get("ServoKepIntakeLeft");
        ServoKepIntakeRight = hardwareMap.servo.get("ServoKepIntakeRight");

        ServoIntakeRight.setDirection(Servo.Direction.REVERSE);
        ServoKepIntakeRight.setDirection(Servo.Direction.REVERSE);
        ServoKepOuttake.setDirection(Servo.Direction.REVERSE);
        ServoKepIntakeLeft.setDirection(Servo.Direction.FORWARD);

        ServoKepIntakeLeft.setPosition(KepIntakeLeftOpenPos);
        ServoKepIntakeRight.setPosition(KepIntakeRightOpenPos);
        ServoKepOuttake.setPosition(KepOuttakeOpenPos);
        resetIntake();
        resetOuttake();

        telemetry.addData(">", "Init Successfully");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while(opModeIsActive()){
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontLeftMotor.setPower(frontLeftPower*speed);
            backLeftMotor.setPower(backLeftPower*speed);
            frontRightMotor.setPower(frontRightPower*speed);
            backRightMotor.setPower(backRightPower*speed);

            if(gamepad1.right_bumper) {
                grabIntake();
            }else if (gamepad1.left_bumper) {
                releaseIntake();
            }

            if(gamepad1.a && !intakeBusy) {
                grabIntake();
                sleep(200);
                ServoIntakePos = IntakeLowPos;
                ServoIntakeLeft.setPosition(ServoIntakePos);
                ServoIntakeRight.setPosition(ServoIntakePos);
            } else if (gamepad1.b && !intakeBusy) {
                grabIntake();
                sleep(200);
                ServoIntakePos = IntakeMidPos;
                ServoIntakeLeft.setPosition(ServoIntakePos);
                ServoIntakeRight.setPosition(ServoIntakePos);
            }

            if(gamepad1.x) {
                resetIntake();
            }
            if(gamepad1.y){
                resetOuttake();
            }
            controlIntakeServos();

            if(gamepad1.dpad_right) {
                grabOuttake();
            } else if (gamepad1.dpad_left) {
                releaseOuttake();
            }
            controlOuttakeServo();

            telemetry.addData("Servo intake pos: ", ServoIntakePos);
            telemetry.addData("Servo outtake pos: ", ServoOuttakePos);

            telemetry.addData("Servo kep intake pos: ", ServoKepIntakeLeft.getPosition());
            telemetry.addData("Servo kep outtake pos: ", ServoKepOuttake.getPosition());

            telemetry.addData("Intake busy: ", intakeBusy);
            telemetry.addData("Outtake busy: ", outtakeBusy);
            telemetry.update();
        }
    }
}
