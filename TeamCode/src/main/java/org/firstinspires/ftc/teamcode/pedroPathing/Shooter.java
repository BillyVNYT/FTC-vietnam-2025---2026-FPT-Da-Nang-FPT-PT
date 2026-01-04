package org.firstinspires.ftc.teamcode.pedroPathing;

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

public class Shooter {
    private DcMotorEx MShooter1, MShooter2;
    private Servo SAngle;
    private LimelightHardware limelight;
    public ElapsedTime timer = new ElapsedTime();
    double P = 15.1;
    double F = 0.0112;
    double[] servoPositions = {0.8492, 0.6389, 0};
    int stepIdx = 0;

    int tprShot = 0;
    int WINDOW = 10;
    double[] buffer = new double[WINDOW];
    int index = 0;
    int count = 0;

    public void initShooter(HardwareMap hardwareMap) {
        MShooter1 = hardwareMap.get(DcMotorEx.class, "0");
        MShooter2 = hardwareMap.get(DcMotorEx.class, "1");
        MShooter1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        MShooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);
        MShooter1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        SAngle = hardwareMap.get(Servo.class, "s0");
        SAngle.setPosition(0.8492);

        limelight = new LimelightHardware(hardwareMap);
        timer.reset();
    }

    int getAverage(double newValue) {
        buffer[index] = newValue;
        index = (index + 1) % WINDOW;
        count = Math.min(count + 1, WINDOW);

        double sum = 0;
        for (int i = 0; i < count; i++) sum += buffer[i];

        return (int) sum / count;
    }

    public void shoot(Telemetry telemetry) {
        double distance = limelight.getAprilTagData().z;
        if(distance <= 95){
            SAngle.setPosition(servoPositions[2]);
            tprShot = (int) (1435.084*Math.pow(distance, 0.06423677));
        } else if (distance <= 200){
            SAngle.setPosition(servoPositions[1]);
            tprShot = (int) (1027.532*Math.pow(distance, 0.1454576));
        } else {
            SAngle.setPosition(servoPositions[0]);
            tprShot = (int) (22.15773*Math.pow(distance, 0.8496951));
        }


        int avgTPR = getAverage(tprShot);
        setVelocity(avgTPR, telemetry);

        telemetry.addData("tpr shot", avgTPR);
        telemetry.addData("Servo angle", SAngle.getPosition());
        telemetry.addData("Distance", limelight.getAprilTagData().z);
        telemetry.addData("Distance by Target pose", limelight.getDistanceByTargetPose());
        telemetry.addData("Fiducial distance", limelight.getAprilTagData().z);
        telemetry.addData("tx", limelight.getAprilTagData().x);
        telemetry.update();
    }

    public void setVelocity(int velocity, Telemetry telemetry){
        MShooter1.setVelocity(velocity);
        MShooter2.setVelocity(velocity);

        double curVelocity = MShooter1.getVelocity();
        double error = velocity - curVelocity;

        telemetry.addData("curTargetVelocity", velocity);
        telemetry.addData("curVelocity", curVelocity);
        telemetry.addData("error", error);
        telemetry.addLine("---------------------------");
    }
}
