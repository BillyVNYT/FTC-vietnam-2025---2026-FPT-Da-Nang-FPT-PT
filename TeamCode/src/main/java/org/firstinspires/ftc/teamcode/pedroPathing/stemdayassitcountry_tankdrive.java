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
    Servo ServoCoTay;
    Servo ServoKhuyuTay;
    Servo ServoTayKep;

    enum State {
        INIT,
        LOW_PIN,
        MID_PIN,
        HIGH_PIN,
        DROP,
    }
    double TayKepOpenPos = 1;
    double TayKepClosePos = 0.5;
    double CoTayInitPos = 1;
    double CoTayGrabPos = 0.7;
    double CoTayGrabHighPos = 0.5;
    double KhuyuTayInitPos = 0.75;
    double KhuyuTayLowPos = 0.32;
    double KhuyuTayMidPos = 0.2;
    double KhuyuTayHighPos = 0;


    double CoTayPos = CoTayInitPos;
    double KhuyuTayPos = KhuyuTayInitPos;
    double TayKepPos = TayKepOpenPos;
    State state = State.INIT;
    boolean actionDone = false;

    void grabPin(double khuyuTayPos) {
        if(!actionDone){
            ServoTayKep.setPosition(TayKepClosePos);
            sleep(400);
            ServoKhuyuTay.setPosition(khuyuTayPos);
            sleep(400);
            if(khuyuTayPos == KhuyuTayHighPos) {
                ServoCoTay.setPosition(CoTayGrabHighPos);
            } else {
                ServoCoTay.setPosition(CoTayGrabPos);
            }
            actionDone = true;
        } else if(gamepad1.right_bumper){
            state = State.DROP;
            actionDone = false;
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        left = hardwareMap.get(DcMotor.class, "leftmotor");
        right = hardwareMap.get(DcMotor.class, "rightmotor");
        ServoCoTay = hardwareMap.servo.get("ServoCoTay");
        ServoKhuyuTay = hardwareMap.servo.get("ServoKhuyuTay");
        ServoTayKep = hardwareMap.servo.get("ServoTayKep");

        ServoKhuyuTay.setPosition(KhuyuTayPos);
        ServoCoTay.setPosition(CoTayPos);
        ServoTayKep.setPosition(TayKepPos);

        telemetry.addData(">", "Init Succesfully");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while(opModeIsActive()){
            drive = gamepad1.right_stick_x;
            turn = gamepad1.left_stick_y;
            rightPower = Range.clip(drive+turn,-1,1);
            leftPower = Range.clip(drive-turn,-1,1);

            switch (state) {
                case INIT:
                    if(!actionDone) {
                        ServoKhuyuTay.setPosition(KhuyuTayInitPos);
                        ServoCoTay.setPosition(CoTayInitPos);
                        actionDone= true;
                    } else if(gamepad1.a){
                        state = State.LOW_PIN;
                        actionDone = false;
                    } else if(gamepad1.b){
                        state = State.MID_PIN;
                        actionDone = false;
                    } else if(gamepad1.y) {
                        state = State.HIGH_PIN;
                        actionDone = false;
                    }
                    break;

                case LOW_PIN:
                    grabPin(KhuyuTayLowPos);
                    break;
                case MID_PIN:
                    grabPin(KhuyuTayMidPos);
                    break;
                case HIGH_PIN:
                    grabPin(KhuyuTayHighPos);
                    break;
                case DROP:
                    if(!actionDone) {
                        ServoTayKep.setPosition(TayKepOpenPos);
                        actionDone = true;
                    } else if(gamepad1.x){
                        state = State.INIT;
                        actionDone = false;
                    }
                    break;
            }

            if(gamepad1.a && CoTayPos >0){
                CoTayPos-=0.0005;
                ServoCoTay.setPosition(CoTayPos);
            }
            if(gamepad1.y && CoTayPos < 1){
                CoTayPos+=0.0005;
                ServoCoTay.setPosition(CoTayPos);
            }
//
//            if(gamepad1.right_bumper && KhuyuTayPos<1){
//                KhuyuTayPos +=0.0005;
//                ServoKhuyuTay.setPosition(KhuyuTayPos);
//            }
//            if(gamepad1.left_bumper && KhuyuTayPos>0){
//                KhuyuTayPos -=0.0005;
//                ServoKhuyuTay.setPosition(KhuyuTayPos);
//            }
//
//
//            if(gamepad1.b && TayKepPos<1){
//                TayKepPos+=0.0005;
//                ServoTayKep.setPosition(TayKepPos);
//            }
//
//            if(gamepad1.x && TayKepPos>0){
//                TayKepPos-=0.0005;
//                ServoTayKep.setPosition(TayKepPos);
//            }

            left.setPower(leftPower);
            right.setPower(rightPower);

            telemetry.addData("Co tay pos: ",CoTayPos);
            telemetry.addData("Khuy tay pos: ", KhuyuTayPos);
            telemetry.addData("Tay kep pos: ", TayKepPos);
            telemetry.addData("State", state);
            telemetry.addData("Right power", rightPower);
            telemetry.addData("Left power", leftPower);

            telemetry.update();
        }
    }
}
