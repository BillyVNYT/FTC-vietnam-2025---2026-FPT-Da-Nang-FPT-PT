package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotor intake;

    boolean active = false;

    public Intake(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotor.class, "m8");
    }

    public boolean isActive() {
        return active;
    }
    public void start(){
        intake.setPower(1);
        active = true;
    }

    public void stop(){
        intake.setPower(0);
        active = false;
    }


}
