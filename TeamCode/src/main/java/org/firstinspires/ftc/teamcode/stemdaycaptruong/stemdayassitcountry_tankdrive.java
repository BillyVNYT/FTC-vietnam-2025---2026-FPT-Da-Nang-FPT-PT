package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp

public class stemdayassitcountry extends LinearOpMode{


    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor left=hardwareMap.get(DcMotor.class, "leftmotor");
        DcMotor right=hardwareMap.get(DcMotor.class, "rightmotor");
        double drive,turn;
        double rightPower=0,leftPower=0;
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
        while(opModeIsActive()){
            drive=gamepad1.right_stick_x;
            turn=gamepad1.left_stick_y;
            rightPower= Range.clip(drive+turn,-1,1);
            leftPower= Range.clip(drive-turn,-1,1);
            left.setPower(leftPower);
            right.setPower(rightPower);
        }
    }
}
