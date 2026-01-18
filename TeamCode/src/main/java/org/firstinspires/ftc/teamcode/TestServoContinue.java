//package org.firstinspires.ftc.teamcode;
//
//
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
////import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.hardware.ServoImplEx; // Often need the 'Ex' implementation for PwmControl
//
//
//@TeleOp(name = "Servo PWM Disable Test", group = "TEST")
//public class TestServoContinue extends LinearOpMode {
//
//    ServoImplEx SPusher1;
//    ServoImplEx SPusher2;
////    public void DualServoSetPos()
//
//    @Override
//    public void runOpMode() {
//        SPusher1 = hardwareMap.get(ServoImplEx.class, "SPusher1");
//        SPusher2 = hardwareMap.get(ServoImplEx.class,"SPusher2");
//        SPusher1.setDirection(Servo.Direction.REVERSE);
//        telemetry.addLine("Init OK");
//        telemetry.update();
//
//        waitForStart();
//        if (isStopRequested()) return;
//
//        while (opModeIsActive()) {
//
//            // ===== BẬT SERVO =====
//            SPusher1.setPwmEnable();     // đảm bảo PWM đang bật
//            SPusher2.setPwmEnable();     // đảm bảo PWM đang bật
//            SPusher1.setPosition(1);   // set position 1
//            SPusher2.setPosition(1);   // set position 1
//            telemetry.addLine("Servo ON (Position = 1)");
//            telemetry.update();
//            sleep(10_000);            // 10 giây
//
//            // ===== TẮT PWM =====
//            SPusher1.setPwmDisable();    // disable PWM
//            SPusher2.setPwmDisable();    // disable PWM
//            telemetry.addLine("PWM DISABLED");
//            telemetry.update();
//            sleep(5_000);             // 5 giây
//        }
//    }
//}