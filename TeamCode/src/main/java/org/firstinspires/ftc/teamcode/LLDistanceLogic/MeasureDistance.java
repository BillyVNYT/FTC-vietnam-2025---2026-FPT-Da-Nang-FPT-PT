package org.firstinspires.ftc.teamcode.LLDistanceLogic;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@TeleOp
public class MeasureDistance extends LinearOpMode {
    double distance;
    private Limelight3A limelight3A;
    private IMU LLimu;
//    public Servo SCanGat1;
//    public Servo SCanGat2;

    @Override
    public void runOpMode(){
        LLimu = hardwareMap.get(IMU.class, "imu");
        LLimu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP)));

        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");

        telemetry.setMsTransmissionInterval(11);
        limelight3A.pipelineSwitch(0);
        limelight3A.start();

        telemetry.addData(">"," Done Init dawg🥀");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            YawPitchRollAngles orientation = LLimu.getRobotYawPitchRollAngles();
            limelight3A.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));

            LLResult llResult = limelight3A.getLatestResult();

            if(llResult != null && llResult.isValid()){
//            telemetry.addData("Calculated distance",distance);
                telemetry.addData("Target x",llResult.getTx());
                telemetry.addData("Target area",llResult.getTa());
            } else {
                telemetry.addData("ll is valid", llResult.isValid());
                telemetry.addData("llResult != null", llResult != null);
            }
            telemetry.update();
        }



    }
}
