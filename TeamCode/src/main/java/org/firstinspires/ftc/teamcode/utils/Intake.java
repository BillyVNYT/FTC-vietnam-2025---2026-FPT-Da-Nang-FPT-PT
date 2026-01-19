package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotor intake;

    boolean active = false;

    public Intake(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotor.class, "m5");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public boolean isActive() {
        return active;
    }
    public void start(){
        intake.setPower(-1);
        active = true;
    }

    public void stop(){
        intake.setPower(0);
        active = false;
    }


}
