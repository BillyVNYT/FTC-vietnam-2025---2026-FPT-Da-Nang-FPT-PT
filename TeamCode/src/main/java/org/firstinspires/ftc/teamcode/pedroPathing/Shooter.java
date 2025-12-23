package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class Shooter extends LinearOpMode {
    public DcMotorEx Mshooter;
    double maxTPS = Mshooter.getMotorType().getAchieveableMaxTicksPerSecond();
    double highVelocity = 1000;
    double lowVelocity = 500;
    double curTargetVelocity = highVelocity;
    double P = 0;
    double F = 1/maxTPS;
    double[] steps = {10.0, 1.0, 0.1, 0.01, 0.001, 0.0001};
    int stepIdx = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        Mshooter = hardwareMap.get(DcMotorEx.class, "0");
        Mshooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Mshooter.setDirection(DcMotorSimple.Direction.REVERSE);

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
                P += steps[stepIdx];
            } else if(gamepad1.dpadDownWasPressed()) {
                P -= steps[stepIdx];
            }

            if(gamepad1.dpadLeftWasPressed()) {
                F -= steps[stepIdx];
            } else if(gamepad1.dpadRightWasPressed()) {
                F += steps[stepIdx];
            }

            PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0.00001, F);
            Mshooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
            Mshooter.setVelocity(curTargetVelocity);

            double curVelocity = Mshooter.getVelocity();
            double error = curTargetVelocity - curVelocity;

            telemetry.addData("curTargetVelocity", curTargetVelocity);
            telemetry.addData("curVelocity", curVelocity);
            telemetry.addData("error", error);
            telemetry.addLine("---------------------------");
            telemetry.addData("F", "%.4f (dpad L/R)", F);
            telemetry.addData("P", "%.4f (dpad U/D)", P);
            telemetry.addData("Step size", "%.4f (B button)", steps[stepIdx]);
        }
    }

    public void setMshooter(int rpm){
        double velocity = rpm*28/60;
        Mshooter.setVelocity(velocity);
    }
}
