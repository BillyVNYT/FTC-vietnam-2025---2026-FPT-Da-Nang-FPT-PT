package org.firstinspires.ftc.teamcode.utils;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class AdjustBarrel {
    private DcMotor BarrelMotor;
    double BarrelMotorPower;

    public AdjustBarrel(HardwareMap hardwareMap) {
        BarrelMotor = hardwareMap.get(DcMotor.class, "BarrelMotor");

        BarrelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BarrelMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    public static void adjustBarrel(double barrelMotorPower) {
    }

    public void adjustBarrel() {
        BarrelMotor.setPower(BarrelMotorPower);
    }
}
