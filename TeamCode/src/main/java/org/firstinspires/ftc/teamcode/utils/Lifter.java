package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Lifter {
    private Servo SLift;

    public Lifter(HardwareMap hardwareMap) {
        SLift = hardwareMap.get(Servo.class, "liftMotor");
    }
    public void lift() {
        SLift.setPosition(0);
    }

    public void lower() {
         SLift.setPosition(1);
    }
}



