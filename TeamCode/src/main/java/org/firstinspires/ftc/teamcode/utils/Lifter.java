package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lifter {
    private DcMotor liftMotor;
    private static final double LIFT_POWER = 0.8;
    private static final double LOWER_POWER = -0.8;

    public Lifter(HardwareMap hardwareMap) {
        liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");

        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setDirection(DcMotor.Direction.FORWARD);
    }
    public void lift() {
            liftMotor.setPower(LIFT_POWER);

        }
    public void lower() {
            liftMotor.setPower(LOWER_POWER);

    }
}



