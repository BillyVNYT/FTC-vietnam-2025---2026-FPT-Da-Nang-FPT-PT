package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    private DcMotor CaiCucXoayXoay;
    private ManualControl manualControl;
    public Intake(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2){
        CaiCucXoayXoay = hardwareMap.get(DcMotor.class, "m8");
        manualControl = new ManualControl(hardwareMap, gamepad1, gamepad2);
    }
    public void Start(){
        CaiCucXoayXoay.setPower(1);
    }
    public void CheckCommandControl() {
        if (manualControl.TakeBall) {
            CaiCucXoayXoay.setPower(1);
        } else {
            CaiCucXoayXoay.setPower(0);
        }
    }
}
