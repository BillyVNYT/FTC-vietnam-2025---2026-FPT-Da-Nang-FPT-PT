package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ManualControl {
    DcMotor MturnOuttake;
    Gamepad gamepad1;
    Gamepad gamepad2;
    public ManualControl(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        MturnOuttake = hardwareMap.get(DcMotor.class, "m6");
    }

    public void ControlTurnOutTake(){
        if(gamepad2.right_trigger >= 0.1){
            MturnOuttake.setPower(gamepad2.right_trigger);
        } else if(gamepad2.left_trigger >= 0.1){
            MturnOuttake.setPower(-gamepad2.left_trigger);
        } else {
            MturnOuttake.setPower(0);
        }
    }
}
