package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;

import java.util.List;

class ApriltagData{
    double x;
    double y;
    double z;
    double area;
    double id;
    ApriltagData(double x, double y, double z, double area, int id){
        this.x = x;
        this.y = y;
        this.z = z;
        this.area = area;
        this.id = id;
    }
}

public class LimelightHardware {
    public Limelight3A limelight;
    public LimelightHardware(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(100);
        limelight.start();
    }

    public void changePipeline(int tab){
        limelight.pipelineSwitch(tab);
        limelight.reloadPipeline();
    }
    public ApriltagData getAprilTagData(){
        LLResult result = limelight.getLatestResult();
        ApriltagData apriltagData = new ApriltagData(0, 0, 0,0, 0);

        if(result != null && result.isValid()) {
            List<FiducialResult> fiducials = result.getFiducialResults();
            for (FiducialResult fiducial : fiducials) {
                double distance = 179.5511*Math.pow(fiducial.getTargetArea(), -0.6507074);
                apriltagData = new ApriltagData(fiducial.getTargetXDegrees(), fiducial.getTargetYDegrees(),
                        distance, fiducial.getTargetArea(), fiducial.getFiducialId());
            }
        }
        return apriltagData;
    }

    public double getDistanceByTargetPose(){
        List<FiducialResult> result = limelight.getLatestResult().getFiducialResults();

        for (FiducialResult fiducial : result) {
            double x = fiducial.getTargetPoseCameraSpace().getPosition().x;
            double y = fiducial.getTargetPoseCameraSpace().getPosition().y;
            double z = fiducial.getTargetPoseCameraSpace().getPosition().z;
            return Math.sqrt(x*x + y*y + z*z);
        }
        return 0;
    }
}
