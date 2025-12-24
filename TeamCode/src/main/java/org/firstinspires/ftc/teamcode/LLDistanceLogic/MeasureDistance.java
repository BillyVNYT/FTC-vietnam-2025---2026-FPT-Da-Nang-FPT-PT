package org.firstinspires.ftc.teamcode.LLDistanceLogic;
import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@TeleOp
public class MeasureDistance extends OpMode{
    private double distance;
    private Limelight3A limelight3A;
    private IMU LLimu;
    public Servo SCanGat1;
    public Servo SCanGat2;
//    private DcMotor Front_Right_Drive;
//    private DcMotor Front_Left_Drive;
//    private DcMotor Back_Right_Drive;
//    private DcMotor Back_Left_Drive;

    @Override
    public void init(){
//        Front_Right_Drive = hardwareMap.get(DcMotor.class, "FrontRight");
//        Front_Left_Drive = hardwareMap.get(DcMotor.class, "FrontLeft");
//        Back_Right_Drive = hardwareMap.get(DcMotor.class, "BackRight");
//        Back_Left_Drive = hardwareMap.get(DcMotor.class, "BackLeft");
        LLimu=hardwareMap.get(IMU.class, "imu");
        limelight3A=hardwareMap.get(Limelight3A.class, "23321LimeLight");
        SCanGat1 = hardwareMap.get(Servo.class, "s4");
        SCanGat2 = hardwareMap.get(Servo.class, "s5");
        limelight3A.pipelineSwitch(0);
    }
    @Override
    public void start(){

//        Front_Left_Drive.setDirection(DcMotorSimple.Diection.REVERSE);
//        Back_Left_Drive.setDirection(DcMotorSimple.Direction.REVERSE);
//        SVuongGoc2.setDirection(Servo.Direction.REVERSE);

        limelight3A.start();
        SCanGat1.setPosition(0.14);
        SCanGat2.setPosition(1-1.24);
        telemetry.addData(">"," Done Init dawg🥀");
        telemetry.update();
    }
    @Override
    public void loop(){
//        double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
//        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
//        double rx = gamepad1.right_stick_x;
//        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
//        double frontLeftPower = (y + x + rx) / denominator;
//        double backLeftPower = (y - x + rx) / denominator;
//        double frontRightPower = (y - x - rx) / denominator;
//        double backRightPower = (y + x - rx) / denominator;
//
//        Front_Left_Drive.setPower(frontLeftPower*0.8);
//        Back_Left_Drive.setPower(backLeftPower*0.8);
//        Front_Right_Drive.setPower(frontRightPower*0.8);
//        Back_Right_Drive.setPower(backRightPower*0.8);
        YawPitchRollAngles orientation = LLimu.getRobotYawPitchRollAngles();
        limelight3A.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
        LLResult result = limelight3A.getLatestResult();
        LLResult llResult = limelight3A.getLatestResult();
        telemetry.addData("LL result: ", llResult);

        if(llResult!=null&& llResult.isValid()){
            Pose3D botpose=llResult.getBotpose_MT2();
            telemetry.addData("Calculated distance",distance);
            telemetry.addData("Target x",llResult.getTx());
            telemetry.addData("Target area",llResult.getTa());
            telemetry.addData("Bot pose", botpose.toString());
        }
        telemetry.update();

    }
}
