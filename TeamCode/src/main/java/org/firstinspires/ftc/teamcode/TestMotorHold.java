package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="Motor Hold Position")
public class TestMotorHold extends LinearOpMode {

    // Sử dụng DcMotorEx để có các tính năng nâng cao nếu cần sau này
    private DcMotorEx armMotor;

    @Override
    public void runOpMode() {
        armMotor = hardwareMap.get(DcMotorEx.class, "m0");

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        moveAndHold(armMotor, 28, 0.5);
        while (opModeIsActive()) {
            double power = -gamepad1.left_stick_y * 0.3;
            armMotor.setPower(power);
            telemetry.addData("Encoder hien tai", armMotor.getCurrentPosition());

            if (power > 0 && armMotor.getCurrentPosition() < 0) {
                telemetry.addLine("Motor dang bi nguoc chieu Encoder -> setDirection REVERSE");
            }

            telemetry.update();
        }

    }

    public void moveAndHold(DcMotorEx motor, int targetPosition, double maxSpeed) {
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(maxSpeed);
    }
}
