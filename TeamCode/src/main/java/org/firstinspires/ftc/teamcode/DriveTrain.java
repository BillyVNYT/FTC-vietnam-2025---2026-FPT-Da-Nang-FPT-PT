package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveTrain {
    DcMotor RightFrontDrive;
    DcMotor RightBackDrive;
    DcMotor LeftFrontDrive;
    DcMotor LeftBackDrive;
    public DriveTrain(HardwareMap hardwareMap){
        RightFrontDrive = hardwareMap.get(DcMotor.class, "m1");
        RightBackDrive = hardwareMap.get(DcMotor.class, "m2");
        LeftFrontDrive = hardwareMap.get(DcMotor.class, "m3");
        LeftBackDrive = hardwareMap.get(DcMotor.class, "m4");
    }

    public void DrivetrainControlBasic(Gamepad gamepad1, Gamepad gamepad2){
        double y = (-gamepad1.left_stick_y - gamepad1.right_stick_y)+(-gamepad2.left_stick_y - gamepad2.right_stick_y);
        double x = gamepad1.left_stick_x + gamepad2.left_stick_x;
        double rx = gamepad1.right_stick_x + gamepad2.right_stick_x;

        LeftFrontDrive.setPower(y + x + rx);
        LeftBackDrive.setPower(y - x + rx);
        RightFrontDrive.setPower(y - x - rx);
        RightBackDrive.setPower(y + x - rx);
    }

    public void DrivetrainControlAdvanced(Gamepad gamepad1, Gamepad gamepad2){

    }
}
