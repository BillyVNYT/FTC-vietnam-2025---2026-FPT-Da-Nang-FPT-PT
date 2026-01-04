package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class mainRed extends LinearOpMode {
    private final LimelightHardware limelight = new LimelightHardware(hardwareMap);
    private DcMotor MOuttakeSpinner;
    double Kp = 1;

    @Override
    public void runOpMode() {
        MOuttakeSpinner = hardwareMap.get(DcMotor.class, "0");
        trackAprilTag();
    }
    public void trackAprilTag(){
        double error = limelight.getAprilTagData().x;
        if(error >= 5 || error <= -5){
            MOuttakeSpinner.setPower(limelight.getAprilTagData().z*Kp);
        } else MOuttakeSpinner.setPower(0);
    }
}
