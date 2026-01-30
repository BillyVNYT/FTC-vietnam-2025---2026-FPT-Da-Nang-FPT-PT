package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.Arrays;
import java.util.List;

class ApriltagData{
    public double x;
    public double y;
    public double z;
    public double area;
    public int id;
    public ApriltagData(double x, double y, double z, double area, int id){
        this.x = x;
        this.y = y;
        this.z = z;
        this.area = area;
        this.id = id;
    }
}
public class LimelightHardware {
    public Limelight3A limelight;
    List<SortBall.BallColor> motif;
    List<SortBall.BallColor> obelisk21 = Arrays.asList(SortBall.BallColor.GREEN, SortBall.BallColor.PURPLE, SortBall.BallColor.PURPLE);
    List<SortBall.BallColor> obelisk22 = Arrays.asList(SortBall.BallColor.PURPLE, SortBall.BallColor.GREEN, SortBall.BallColor.PURPLE);
    List<SortBall.BallColor> obelisk23 = Arrays.asList(SortBall.BallColor.PURPLE, SortBall.BallColor.PURPLE, SortBall.BallColor.GREEN);

    public LimelightHardware(HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(100);
        limelight.start();
    }

    public List<SortBall.BallColor> getMotif() {
        return motif;
    }

    public void setMotif(int id) {
        if (id < 21) return;

        switch (id) {
            case 21:
                motif = obelisk21;
                break;
            case 22:
                motif = obelisk22;
                break;
            case 23:
                motif = obelisk23;
                break;
        }
    }

    public void changePipeline(int tab){
        limelight.pipelineSwitch(tab);
        limelight.reloadPipeline();
    }
    public ApriltagData getAprilTagData(Telemetry telemetry){
        LLResult result = limelight.getLatestResult();
        ApriltagData apriltagData;

        if(result != null && result.isValid()) {
            List<FiducialResult> fiducials = result.getFiducialResults();
            for (FiducialResult fiducial : fiducials) {
                double distance = 0;
                int id = fiducial.getFiducialId();
                if(id == 24 || id == 20) {
                    distance = 172.08*fiducial.getTargetPoseCameraSpace().getPosition().z - 16.816;
                } else if (id >= 21 && id < 24 && motif == null) {
                    setMotif(id);
                }

                apriltagData = new ApriltagData(result.getTx(), result.getTy(),
                        distance, result.getTa(), id);
                return apriltagData;
            }
        }
        return null;
    }
    public double getTx(int Id){
        LLResult result = limelight.getLatestResult();
        if(result != null && result.isValid()){
            List<FiducialResult> fiducials = result.getFiducialResults();
            for (FiducialResult fiducial : fiducials) {
                if(fiducial.getFiducialId() == Id) return result.getTx();
            }
        }
        return 0;
    }
}
