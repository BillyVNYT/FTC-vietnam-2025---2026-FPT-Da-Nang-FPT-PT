package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class test extends LinearOpMode {
    Servo s1;
    Servo s2;
    @Override
    public void runOpMode() throws InterruptedException {
        s1 = hardwareMap.get(Servo.class, "1");
        s2 = hardwareMap.get(Servo.class, "2");
        waitForStart();
        while (opModeIsActive()){
            s1.setPosition(1);
            s2.setPosition(0);
            sleep(500);
            s1.setPosition(0.9);
            s2.setPosition(0.1);
        }
    }
}