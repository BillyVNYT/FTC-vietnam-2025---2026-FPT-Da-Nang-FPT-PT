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
    double area;
    double id;
    ApriltagData(double x, double y, double area, int id){
        this.x = x;
        this.y = y;
        this.area = area;
        this.id = id;
    }
}

public class LimelightHardware {
    public Limelight3A limelight;
    private static final double CAMERA_HEIGHT = 0.08;  // 8 cm
    private static final double TAG_HEIGHT    = 0.75;  // ví dụ tag trên backdrop
    private static final double CAMERA_PITCH  = 39.0;  // độ (ước lượng từ ảnh)
    public LimelightHardware(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(100);
        limelight.start();
    }

    public void changePileline(int tab){
        limelight.pipelineSwitch(tab);
        limelight.reloadPipeline();
    }
    public ApriltagData getAprilTagData(){
        LLResult result = limelight.getLatestResult();

        if(result != null && result.isValid()){
            List<FiducialResult> fiducials = result.getFiducialResults();
            for (FiducialResult fiducial : fiducials){
                return new ApriltagData(result.getTx(), result.getTy(), result.getTa(), fiducial.getFiducialId());
            }
        }
        return new ApriltagData(0, 0, 0, 0);
    }
    public double getDistance(){
        LLResult result = limelight.getLatestResult();

        if(result != null && result.isValid()){
            List<FiducialResult> fiducials = result.getFiducialResults();
            for (FiducialResult fiducial : fiducials){
                return 179.5511*Math.pow(result.getTa(),-0.6507074);
            }
        }
        return 0;
    }
    public double getDistanceByTargetPose(){
        List<FiducialResult> result = limelight.getLatestResult().getFiducialResults();

        for (FiducialResult fiducial : result) {
            double x = fiducial.getTargetPoseCameraSpace().getPosition().x;
            double y = fiducial.getTargetPoseCameraSpace().getPosition().y;
            double z = fiducial.getTargetPoseCameraSpace().getPosition().z;
            double angleRad = Math.toRadians(CAMERA_PITCH + y);
            double heightDiff = TAG_HEIGHT - CAMERA_HEIGHT;

            if (Math.abs(Math.tan(angleRad)) < 1e-3) return -1;

            return heightDiff / Math.tan(angleRad);
        }
        return 0;
    }
}
