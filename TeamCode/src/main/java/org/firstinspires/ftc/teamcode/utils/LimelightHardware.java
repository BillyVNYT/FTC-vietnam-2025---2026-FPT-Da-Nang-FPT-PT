package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.utils.ApriltagData;

import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;

import java.util.List;
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
                double distance = 62 * Math.pow(fiducial.getTargetPoseCameraSpace().getPosition().z, 0.92);

                apriltagData = new ApriltagData(result.getTx(), result.getTy(),
                        distance, result.getTa(), fiducial.getFiducialId());
            }
        }

        return apriltagData;
    }
}
