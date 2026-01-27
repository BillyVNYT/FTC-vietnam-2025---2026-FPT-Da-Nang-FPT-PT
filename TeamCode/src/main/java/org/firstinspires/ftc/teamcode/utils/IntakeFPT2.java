package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.security.Provider;

public class IntakeFPT2 {
    private DcMotor intake;
    private Servo Spush;
    private Servo SHoldball1;
    private Servo SHoldball2;

    public boolean HoldBall = true;

    public IntakeFPT2(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotor.class, "m4");
        Spush = hardwareMap.get(Servo.class, "s8");
        SHoldball1 = hardwareMap.get(Servo.class, "s9");
        SHoldball2 = hardwareMap.get(Servo.class, "s4");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        Spush.setDirection(Servo.Direction.REVERSE);
        SHoldball2.setDirection(Servo.Direction.REVERSE);
        Spush.setPosition(0);
        SHoldball1.setPosition(0);
        SHoldball2.setPosition(0);
    }
    public void updateIntakeManual(Gamepad gamepad){
        if(gamepad.left_bumper){
            HoldBall = true;
            intake.setPower(1);
        } else if(gamepad.left_trigger > 0.5) {
            intake.setPower(-1);
        } else {
            intake.setPower(0);
        }
    }
    public boolean isActive(){
        intake.setPower(1);
        return false;
    }
    public void stop(){
        intake.setPower(0);
    }
    public void checkHoldBall(){
        if(HoldBall){
            Spush.setPosition(0.2);
            SHoldball1.setPosition(0.08);
            SHoldball2.setPosition(0.08);
        } else {
            Spush.setPosition(0);
            SHoldball1.setPosition(0);
            SHoldball2.setPosition(0);
        }
    }
}
