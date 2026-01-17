package org.firstinspires.ftc.teamcode.utils;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class TestShooter extends LinearOpMode{
      DcMotor MShooter2;
      DcMotor MShooter1;
//    DcMotorEx MShooter1;
//    DcMotorEx MShooter2;
//    double P = 15.1;
//    double F = 0.0112;
//    double Kp = 1;
//    public void setMotorVelocity(int velocity, Telemetry telemetry){
//        MShooter1.setVelocity(velocity);
//        MShooter2.setVelocity(velocity);
//
//        double curVelocity = MShooter1.getVelocity();
//        double error = velocity - curVelocity;
//
//        telemetry.addData("curTargetVelocity", velocity);
//        telemetry.addData("curVelocity", curVelocity);
//        telemetry.addData("error", error);
//        telemetry.addLine("---------------------------");
//    }
    @Override
    public void runOpMode() throws InterruptedException{
//        ManualControl2 manualControl2 = new ManualControl2(hardwareMap, gamepad2);
//        MShooter1 = hardwareMap.get(DcMotorEx.class, "m5");
//        MShooter2 = hardwareMap.get(DcMotorEx.class, "m6");
//        MShooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        MShooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        MShooter1.setDirection(DcMotorSimple.Direction.REVERSE);
//        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);
//        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
//
//        waitForStart();
//        while(opModeIsActive()){
//            setMotorVelocity(2436,telemetry);
//            telemetry.update();
//        }
          MShooter2=hardwareMap.get(DcMotor.class,"m5");
          MShooter1=hardwareMap.get(DcMotor.class,"m6");
          MShooter1.setDirection(DcMotorSimple.Direction.REVERSE);
          waitForStart();
          while(opModeIsActive()){
              MShooter1.setPower(1);
              MShooter2.setPower(1);
          }


    }
}
