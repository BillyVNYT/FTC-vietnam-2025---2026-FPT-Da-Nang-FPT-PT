package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class Shooter extends LinearOpMode {
    public DcMotorEx Mshooter1;
    public DcMotorEx Mshooter2;
    double maxTPS;
    double highVelocity = 2000;
    double lowVelocity = 1000;
    double curTargetVelocity = highVelocity;
    double P = 15.1;
    double F = 0.0112;
    double[] steps = {1000, 500, 200, 100, 50, 25};
    int stepIdx = 3;


    @Override
    public void runOpMode() throws InterruptedException {
        Mshooter1 = hardwareMap.get(DcMotorEx.class, "0");
        Mshooter2 = hardwareMap.get(DcMotorEx.class, "1");
        Mshooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Mshooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        Mshooter1.setDirection(DcMotorSimple.Direction.REVERSE);
        Mshooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()){
            if(gamepad1.aWasPressed()) {
                if(curTargetVelocity == highVelocity) {
                    curTargetVelocity = lowVelocity;
                } else { curTargetVelocity = highVelocity; }
            }

            if(gamepad1.bWasPressed()) {
                stepIdx = (stepIdx + 1) % steps.length;
            }

            if(gamepad1.dpadUpWasPressed()) {
                curTargetVelocity+= steps[stepIdx];
            } else if(gamepad1.dpadDownWasPressed()) {
                curTargetVelocity -= steps[stepIdx];
            }

            PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);
            Mshooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
            Mshooter1.setVelocity(curTargetVelocity);
            Mshooter2.setVelocity(curTargetVelocity);

            double curVelocity = Mshooter1.getVelocity();
            double error = curTargetVelocity - curVelocity;

            telemetry.addData("curTargetVelocity", curTargetVelocity);
            telemetry.addData("curVelocity", curVelocity);
            telemetry.addData("error", error);
            telemetry.addLine("---------------------------");
            telemetry.addData("F", "%.4f (dpad L/R)", F);
            telemetry.addData("P", "%.4f (dpad U/D)", P);
            telemetry.addData("Step size", "%.4f (B button)", steps[stepIdx]);
            telemetry.update();
        }
    }

    public void setMshooter(int rpm){
        double velocity = rpm*28/60;
        Mshooter1.setVelocity(velocity);
    }
}
