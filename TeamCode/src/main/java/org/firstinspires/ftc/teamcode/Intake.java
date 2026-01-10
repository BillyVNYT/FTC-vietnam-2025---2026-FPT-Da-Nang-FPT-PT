package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotor intake;
    private ManualControl manualControl;
    public Intake(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        intake = hardwareMap.get(DcMotor.class, "m8");
        manualControl = new ManualControl(hardwareMap, gamepad1, gamepad2);
    }
    public void start(){
        intake.setPower(1);
    }
    public void toggleIntake() {
        if (manualControl.takeBall) {
            intake.setPower(1);
        } else {
            intake.setPower(0);
        }
    }
}
