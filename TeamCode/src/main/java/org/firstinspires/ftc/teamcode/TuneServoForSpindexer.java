package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TuneServoForSpindexer extends LinearOpMode {
    Servo spindexer1, spindexer2;
    @Override
    public void runOpMode() throws InterruptedException {
        spindexer1 = hardwareMap.get(Servo.class, "s0");
        spindexer2 = hardwareMap.get(Servo.class, "s1");

        spindexer2.setDirection(Servo.Direction.REVERSE);
        waitForStart();
        while (opModeIsActive()){
            if (gamepad1.right_bumper) {
                spindexer1.setPosition(spindexer1.getPosition()+0.0002);
                spindexer2.setPosition(spindexer2.getPosition()+0.0002);
            }
            if (gamepad1.left_bumper) {
                spindexer1.setPosition(spindexer1.getPosition()-0.0002);
                spindexer2.setPosition(spindexer2.getPosition()-0.0002);
            }
            telemetry.addData("pos", spindexer1.getPosition());
            telemetry.update();
        }
    }
}
