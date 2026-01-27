package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

//public class ApriltagData {
//    public final double tx;
//    public final double ty;
//    public final double distance;
//    public final double ta;
//    public Limelight3A limelight;
//    public final double fiducialId;
//
//    /**
//     * A container for AprilTag data processed from the Limelight.
//     *
//     * @param tx         The horizontal offset from the crosshair to the target (in degrees).
//     * @param ty         The vertical offset from the crosshair to the target (in degrees).
//     * @param distance   The calculated distance to the target.
//     * @param ta         The target area (0% to 100% of the screen).
//     * @param fiducialId The ID of the detected AprilTag.
//     */
//    public ApriltagData(double tx, double ty, double distance, double ta, double fiducialId) {
//        this.tx = tx;
//        this.ty = ty;
//        this.distance = distance;
//        this.ta = ta;
//        this.fiducialId = fiducialId;
//    }
//    public ApriltagData getAprilTagData(Telemetry telemetry){
//        LLResult result = limelight.getLatestResult();
//        ApriltagData apriltagData = new ApriltagData(0, 0, 0,0, 0);
//
//        if(result != null && result.isValid()) {
//            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
//            for (LLResultTypes.FiducialResult fiducial : fiducials) {
//                double distance = 0;
//                if(fiducial.getFiducialId() == 24|| fiducial.getFiducialId() == 20){
//                    distance = 186*fiducial.getTargetPoseCameraSpace().getPosition().z-38.5;;
//                }
//                telemetry.update();
//                apriltagData = new ApriltagData(result.getTx(), result.getTy(),
//                        distance, result.getTa(), fiducial.getFiducialId());
//                return apriltagData;
//            }
//        }
//        return null;
//    }
//}