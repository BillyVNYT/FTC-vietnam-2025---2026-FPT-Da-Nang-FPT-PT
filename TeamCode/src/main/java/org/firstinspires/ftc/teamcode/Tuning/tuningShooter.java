package org.firstinspires.ftc.teamcode.Tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.utils.IntakeFPT2;
import org.firstinspires.ftc.teamcode.utils.SafeShoot;

import java.util.ArrayList;
import java.util.List;

@TeleOp
public class tuningShooter extends LinearOpMode {
    private SafeShoot shooter;
    private IntakeFPT2 intake;
    ArrayList<Double> list_change = new ArrayList<>();
    int i = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        intake = new IntakeFPT2(hardwareMap);
        shooter = new SafeShoot(hardwareMap, intake);
        list_change.add(0.001);
        list_change.add(0.01);
        list_change.add(0.1);
        list_change.add(1.0);
        waitForStart();
        shooter.setMotorVelocity(2000);
        while (opModeIsActive()){
            if(gamepad2.dpadUpWasPressed()){
                shooter.P += list_change.get(i);
            } else if(gamepad2.dpadDownWasPressed()){
                shooter.P -= list_change.get(i);
            }
            if(gamepad2.dpadLeftWasPressed()){
                shooter.I += list_change.get(i);
            } else if(gamepad2.dpadRightWasPressed()){
                shooter.I -= list_change.get(i);
            }
            if(gamepad2.circleWasPressed()){
                shooter.D += list_change.get(i);
            } else if(gamepad2.squareWasPressed()){
                shooter.D -= list_change.get(i);
            }
            if(gamepad2.triangleWasPressed()){
                shooter.F += list_change.get(i);
            } else if(gamepad2.crossWasPressed()){
                shooter.F -= list_change.get(i);
            }
            if(gamepad2.right_bumper){
                shooter.setMotorVelocity(0);
            }
            if(gamepad2.left_bumper){
                shooter.setMotorVelocity(2000);
                PIDFCoefficients pidf = new PIDFCoefficients(shooter.P, shooter.I, shooter.D, shooter.F);
                shooter.MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
                shooter.MShooter2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
            }
            if(gamepad1.dpadUpWasPressed()){
                i++;
            } else if(gamepad1.dpadDownWasPressed()){
                i--;
            }
            telemetry.addData("P", shooter.P);
            telemetry.addData("I", shooter.I);
            telemetry.addData("D", shooter.D);
            telemetry.addData("F", shooter.F);
            telemetry.addData("Add", list_change.get(i));
            telemetry.addData("error", 2000-shooter.MShooter1.getVelocity());
            telemetry.update();
        }
    }
}
