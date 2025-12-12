package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class stemdayassitcountry_tankdrive extends LinearOpMode{
    double drive, turn;
    double rightPower=0,leftPower=0;


    DcMotor left;
    DcMotor right;
    Servo ServoKhuyuTay;
    Servo ServoTayKep;
    double TayKepOpenPos = 0;
    double KhuyuTayLowPos = 0.01;


    double KhuyuTayPos = KhuyuTayLowPos;
    double TayKepPos = TayKepOpenPos;

    @Override
    public void runOpMode() throws InterruptedException {
        left = hardwareMap.get(DcMotor.class, "leftmotor");
        right = hardwareMap.get(DcMotor.class, "rightmotor");
        ServoKhuyuTay = hardwareMap.servo.get("ServoKhuyuTay");
        ServoTayKep = hardwareMap.servo.get("ServoTayKep");

        ServoKhuyuTay.setDirection(Servo.Direction.REVERSE);
        ServoKhuyuTay.setPosition(KhuyuTayPos);
        ServoTayKep.setPosition(TayKepPos);

        telemetry.addData(">", "Init Successfully");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while(opModeIsActive()){
            drive = gamepad1.right_stick_x;
            turn = gamepad1.left_stick_y;
            rightPower = Range.clip(drive+turn,-1,1);
            leftPower = Range.clip(drive-turn,-1,1);

            if(gamepad1.right_bumper && KhuyuTayPos<1){
                KhuyuTayPos +=0.0005;
                ServoKhuyuTay.setPosition(KhuyuTayPos);
            }
            if(gamepad1.left_bumper && KhuyuTayPos>0){
                KhuyuTayPos -=0.0005;
                ServoKhuyuTay.setPosition(KhuyuTayPos);
            }

            if(gamepad1.b && TayKepPos<1){
                TayKepPos+=0.0005;
                ServoTayKep.setPosition(TayKepPos);
            }

            if(gamepad1.x && TayKepPos>0){
                TayKepPos-=0.0005;
                ServoTayKep.setPosition(TayKepPos);
            }

            left.setPower(leftPower);
            right.setPower(rightPower);

            telemetry.addData("Khuy tay pos: ", KhuyuTayPos);
            telemetry.addData("Tay kep pos: ", TayKepPos);
            telemetry.addData("Right power", rightPower);
            telemetry.addData("Left power", leftPower);

            telemetry.update();
        }
    }
}
