package org.firstinspires.ftc.teamcode.pedroPathing;

import android.icu.text.UnicodeSet;

import com.bylazar.panels.Panels;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class Shooter extends LinearOpMode {
    public DcMotorEx Mshooter1;
    public DcMotorEx Mshooter2;
    public Servo Sdegree;
    double P = 15.1;
    double F = 0.0112;
    double[] stepsServo = {0.8492, 0.6389, 0};
    int stepIdx = 0;
    public ElapsedTime timer = new ElapsedTime();

    int tprShot = 0;
    private PanelsTelemetry panelsTelemetry;
    private LimelightHardware limelightHardware;

    @Override
    public void runOpMode() throws InterruptedException {
        Mshooter1 = hardwareMap.get(DcMotorEx.class, "0");
        Mshooter2 = hardwareMap.get(DcMotorEx.class, "1");
        Mshooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Mshooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        Mshooter1.setDirection(DcMotorSimple.Direction.REVERSE);
        Mshooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        Sdegree = hardwareMap.get(Servo.class, "s0");

        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);
        Mshooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        limelightHardware = new LimelightHardware(hardwareMap);

        waitForStart();
        Sdegree.setPosition(0.8492);
        timer.reset();
        while (opModeIsActive()){
            if(limelightHardware.getDistance() <= 85){
                tprShot = (int) (1435.084*Math.pow(limelightHardware.getDistance(), 0.06423677));
            } else if (limelightHardware.getDistance() <= 180){
                tprShot = (int) (1027.532*Math.pow(limelightHardware.getDistance(), 0.1454576));
            } else {
                tprShot = (int) (22.15773*Math.pow(limelightHardware.getDistance(), 0.8496951));
            }
            telemetry.addData("s0", Sdegree.getPosition());
            telemetry.addData("dis", limelightHardware.getDistance());
            telemetry.addData("obj", limelightHardware.getAprilTagData().area);
            telemetry.addData("tpr shot", tprShot);
//            panelsTelemetry.getTelemetry().addData("s0", Sdegree.getPosition());
//            panelsTelemetry.getTelemetry().addData("dis", limelightHardware.getDistance());
//            panelsTelemetry.getTelemetry().addData("tpr shot", tprShot);
            telemetry.update();
//            panelsTelemetry.getTelemetry().update();
        }
    }

    public void setMshooter(int rpm){
        double velocity = rpm*28/60;
        Mshooter1.setVelocity(velocity);
        Mshooter2.setVelocity(velocity);

        double curVelocity = Mshooter1.getVelocity();
        double error = velocity - curVelocity;

        telemetry.addData("curTargetVelocity", velocity);
        telemetry.addData("curVelocity", curVelocity);
        telemetry.addData("error", error);
        telemetry.addLine("---------------------------");
        telemetry.addData("F", "%.4f (dpad L/R)", F);
        telemetry.addData("P", "%.4f (dpad U/D)", P);
        telemetry.addData("Step size", "%.4f (B button)", stepsServo[stepIdx]);

//        panelsTelemetry.getFtcTelemetry().addData("curTargetVelocity", velocity);
//        panelsTelemetry.getFtcTelemetry().addData("curVelocity", curVelocity);
//        panelsTelemetry.getFtcTelemetry().addData("error", error);
//        panelsTelemetry.getFtcTelemetry().addLine("---------------------------");
//        panelsTelemetry.getFtcTelemetry().addData("F", "%.4f (dpad L/R)", F);
//        panelsTelemetry.getFtcTelemetry().addData("P", "%.4f (dpad U/D)", P);
//        panelsTelemetry.getFtcTelemetry().addData("Step size", "%.4f (B button)", stepsServo[stepIdx]);
    }
}
