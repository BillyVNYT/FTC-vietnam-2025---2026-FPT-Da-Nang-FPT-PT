package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ganhTem_assistCountry extends LinearOpMode{
    DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;

    Servo ServoIntakeLeft, ServoIntakeRight;
    Servo ServoKepIntakeLeft, ServoKepIntakeRight;
    Servo ServoOuttake, ServoKepOuttake;

    double KepIntakeClosePos = 0.3;
    double KepIntakeOpenPos = 1;
    double KepOuttakeClosePos = 0;
    double KepOuttakeOpenPos = 0.5;

    double OuttakeInitPos = 0.2;
    double IntakeInitPos = 0;
    double ServoIntakePos = IntakeInitPos;
    double ServoOuttakePos = OuttakeInitPos;

    double OuttakeRaisePos = 0.1;

    double IntakeLowPos = 0.1;
    double IntakeMidPos = 0.2;
    final double speed = 1.0;

    boolean intakeBusy = false;
    boolean outtakeBusy = false;


    void grabIntake() {
        if(!intakeBusy) {
            ServoKepIntakeLeft.setPosition(KepIntakeClosePos);
            ServoKepIntakeRight.setPosition(KepIntakeClosePos);
            intakeBusy = true;
        }
    }

    void releaseIntake() {
        if(intakeBusy) {
            ServoKepIntakeLeft.setPosition(KepIntakeOpenPos);
            ServoKepIntakeRight.setPosition(KepIntakeOpenPos);
            intakeBusy = false;
        }
    }

    void grabOuttake() {
        if(!outtakeBusy) {
            ServoKepOuttake.setPosition(KepOuttakeClosePos);
            ServoOuttake.setPosition(OuttakeRaisePos);
            outtakeBusy = true;
        }
    }

    void releaseOuttake() {
        if(outtakeBusy) {
            ServoKepOuttake.setPosition(KepOuttakeOpenPos);
            ServoOuttake.setPosition(OuttakeInitPos);
            outtakeBusy = false;
        }
    }

    void controlIntakeServos() {
        if(gamepad1.right_trigger > 0 && ServoIntakePos < 1) {
            ServoIntakePos += 0.0005;
            ServoIntakeLeft.setPosition(ServoIntakePos);
            ServoIntakeRight.setPosition(ServoIntakePos);
        }

        if (gamepad1.left_trigger > 0 && ServoIntakePos > 0) {
            ServoIntakePos -= 0.0005;
            ServoIntakeLeft.setPosition(ServoIntakePos);
            ServoIntakeRight.setPosition(ServoIntakePos);
        }
    }

    void controlOuttakeServo () {
        if(gamepad1.dpad_up && ServoOuttakePos < 1) {
            ServoOuttakePos += 0.0005;
            ServoOuttake.setPosition(ServoOuttakePos);
        }

        if (gamepad1.dpad_down && ServoOuttakePos > 0) {
            ServoOuttakePos -= 0.0005;
            ServoOuttake.setPosition(ServoOuttakePos);
        }
    }

    void resetIntake() {
        if(!intakeBusy) {
            ServoIntakeLeft.setPosition(IntakeInitPos);
            ServoIntakeRight.setPosition(IntakeInitPos);
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

        ServoKepOuttake.setDirection(Servo.Direction.REVERSE);
        ServoIntakeRight.setDirection(Servo.Direction.REVERSE);
        ServoKepIntakeLeft.setDirection(Servo.Direction.REVERSE);

        ServoKepIntakeLeft.setPosition(KepIntakeOpenPos);
        ServoKepIntakeRight.setPosition(KepIntakeOpenPos);
        resetIntake();
        ServoOuttake.setPosition(ServoOuttakePos);
        ServoKepOuttake.setPosition(KepOuttakeOpenPos);

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

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
                ServoIntakeLeft.setPosition(IntakeLowPos);
                ServoIntakeRight.setPosition(IntakeLowPos);
            } else if (gamepad1.b && !intakeBusy) {
                grabIntake();
                ServoIntakeLeft.setPosition(IntakeMidPos);
                ServoIntakeRight.setPosition(IntakeMidPos);
            }
            if(gamepad1.x && !intakeBusy) {
                resetIntake();
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
            telemetry.addData("Servo kep intake pos: ", ServoKepIntakeRight.getPosition());
//            telemetry.addData("Servo kep outtake pos: ", ServoKepOuttake.getPosition());
            telemetry.update();
        }
    }
}
