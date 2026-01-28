package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Lifter {
    private Servo SLift1, SLift2;

    public Lifter(HardwareMap hardwareMap) {
        SLift1 = hardwareMap.get(Servo.class, "s0");
        SLift2 = hardwareMap.get(Servo.class, "s6");
        SLift2.setDirection(Servo.Direction.REVERSE);
        SLift1.setPosition(0);
        SLift2.setPosition(0);
    }
    public void lift() {
        SLift1.setPosition(0);
        SLift2.setPosition(0);
    }

    public void lower() {
         SLift1.setPosition(0.18);
         SLift2.setPosition(0.18);
    }
}



