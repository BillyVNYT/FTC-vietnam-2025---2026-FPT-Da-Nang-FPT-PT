package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp
public class ResetServo extends  LinearOpMode{
    Servo s1,s2,s3,s4,s5,s6;


    @Override
    public void runOpMode() throws InterruptedException{
        s1 = hardwareMap.get(Servo.class, "s1");
        s2 = hardwareMap.get(Servo.class,"s2");
        s3 = hardwareMap.get(Servo.class,"s3");
        s4 = hardwareMap.get(Servo.class,"s4");
        s5 = hardwareMap.get(Servo.class,"s5");
        s6 = hardwareMap.get(Servo.class,"s6");

        waitForStart();
        while(opModeIsActive()){
            s1.setPosition(0);
            s2.setPosition(0);
            s3.setPosition(0);
        }
    }
}
