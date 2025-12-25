package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
    int WINDOW = 10;
    double[] buffer = new double[WINDOW];
    int index = 0;
    int count = 0;

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
            if(limelightHardware.getDistance() <= 95){
                Sdegree.setPosition(stepsServo[2]);
                tprShot = (int) (1435.084*Math.pow(limelightHardware.getDistance(), 0.06423677));
            } else if (limelightHardware.getDistance() <= 200){
                Sdegree.setPosition(stepsServo[1]);
                tprShot = (int) (1027.532*Math.pow(limelightHardware.getDistance(), 0.1454576));
            } else {
                Sdegree.setPosition(stepsServo[0]);
                tprShot = (int) (22.15773*Math.pow(limelightHardware.getDistance(), 0.8496951));
            }
            setMshooter(getAverage(tprShot));
            double sum = 0;
            for (int i = 0; i < count; i++) sum += buffer[i];
            telemetry.addData("s0", Sdegree.getPosition());
            telemetry.addData("dis", limelightHardware.getDistance());
            telemetry.addData("obj", limelightHardware.getAprilTagData().area);
            telemetry.addData("tpr shot", sum / count);
            telemetry.addData("tx", limelightHardware.getAprilTagData().x);
//            panelsTelemetry.getTelemetry().addData("s0", Sdegree.getPosition());
//            panelsTelemetry.getTelemetry().addData("dis", limelightHardware.getDistance());
//            panelsTelemetry.getTelemetry().addData("tpr shot", tprShot);
            telemetry.update();
//            panelsTelemetry.getTelemetry().update();
        }
    }
    public void setMshooter(int velocity){
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
    int getAverage(double newValue) {
        buffer[index] = newValue;
        index = (index + 1) % WINDOW;
        count = Math.min(count + 1, WINDOW);

        double sum = 0;
        for (int i = 0; i < count; i++) sum += buffer[i];
        return (int) (sum / count);
    }
}
