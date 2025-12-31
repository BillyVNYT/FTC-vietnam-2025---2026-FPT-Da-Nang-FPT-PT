package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class Outtake {
    public DcMotorEx Mshooter1;
    public DcMotorEx Mshooter2;
    public DcMotorEx Mpreshoot;
    public Servo Sdegree;
    public Servo Spreshoot;
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

    public Outtake(HardwareMap hardwareMap, Telemetry telemetry) {
        Mshooter1 = hardwareMap.get(DcMotorEx.class, "m3");
        Mshooter2 = hardwareMap.get(DcMotorEx.class, "m4");
        Mpreshoot = hardwareMap.get(DcMotorEx.class, "m7");
        Sdegree = hardwareMap.get(Servo.class, "s1");
        Spreshoot = hardwareMap.get(Servo.class, "s2");

        Mshooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Mshooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        Mshooter1.setDirection(DcMotorSimple.Direction.REVERSE);
        Mshooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0.000001, F);
        Mshooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        Mpreshoot.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        limelightHardware = new LimelightHardware(hardwareMap);
    }

    public void Shoot() throws InterruptedException {
        if (limelightHardware.getDistanceByTargetPose() <= 95) {
            Sdegree.setPosition(stepsServo[2]);
            tprShot = (int) (1435.084 * Math.pow(limelightHardware.getDistance(), 0.06423677));
        } else if (limelightHardware.getDistanceByTargetPose() <= 200) {
            Sdegree.setPosition(stepsServo[1]);
            tprShot = (int) (1027.532 * Math.pow(limelightHardware.getDistance(), 0.1454576));
        } else {
            Sdegree.setPosition(stepsServo[0]);
            tprShot = (int) (22.15773 * Math.pow(limelightHardware.getDistance(), 0.8496951));
        }
        setMshooter(tprShot);
        wait(1000);
        Spreshoot.setPosition(0.5);
        wait(1000);
        Spreshoot.setPosition(0);
        Mshooter1.setPower(0);
        Mshooter2.setPower(0);
        Mpreshoot.setPower(0);
        wait(1000);
    }
    public void setMshooter(int velocity){
        Mshooter1.setVelocity(velocity);
        Mshooter2.setVelocity(velocity);
        Mpreshoot.setVelocity(300);
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
