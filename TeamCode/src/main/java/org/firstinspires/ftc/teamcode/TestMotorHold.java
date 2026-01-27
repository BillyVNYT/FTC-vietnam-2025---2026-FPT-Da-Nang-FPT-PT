package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Motor Hold Position")
public class TestMotorHold extends LinearOpMode {

    private DcMotorEx armMotor;
    boolean reversed = false;

    @Override
    public void runOpMode() {
        armMotor = hardwareMap.get(DcMotorEx.class, "m0");

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        moveAndHold(armMotor, 140, 1);

        while (opModeIsActive()) {
//            armMotor.setPower(0.5*(reversed ? 1 : -1));
//            sleep(200);
//            armMotor.setPower(0);
//            sleep(1000);

            telemetry.addData("Encoder hien tai", armMotor.getCurrentPosition());
            telemetry.update();
        }

    }

    public void moveAndHold(DcMotorEx motor, int targetPosition, double maxSpeed) {
        motor.setTargetPosition(targetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(maxSpeed);
    }
}
