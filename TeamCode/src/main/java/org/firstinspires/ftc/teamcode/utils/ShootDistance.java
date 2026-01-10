package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class ShootDistance {
    private Servo shootDistance;

    public ShootDistance(HardwareMap hardwareMap) {
        shootDistance = hardwareMap.get(Servo.class, "ShootDistance");
        shootDistance.setDirection(Servo.Direction.FORWARD);
    }

    public void ChangeDistanceUp(double shootDistanceUp) {
        shootDistance.setPosition(shootDistanceUp);

    }
    public void ChangeDistanceDown(double shootDistanceDown) {
        shootDistance.setPosition(shootDistanceDown);

    }
}
