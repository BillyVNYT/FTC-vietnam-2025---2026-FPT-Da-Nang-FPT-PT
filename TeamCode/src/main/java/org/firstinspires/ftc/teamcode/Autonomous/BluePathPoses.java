package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class BluePathPoses {
    public static Pose topZonePose = new Pose(63.469194312796205, 70.63507109004739);
    public static Pose pickupMidPose = new Pose(10.66589629675898, 54.67825604909545);
    public static Pose pickupGatePose = new Pose(5.32985625834459, 61.00580119771699);
    public static Pose pickupTopPose = new Pose(18.949289099526066, 83.943);
    public static Pose pickupLowPose = new Pose(8.530805687203792, 36.0);
    public static Pose lowZonePose = new Pose(59.54502369668247, 8.530805687203792);
    public static Pose leavePose= new Pose(21.32701421800948, 69.44075829383885);

    public static Pose[] topZoneToPickupMidPoses = {topZonePose, new Pose(58.692, 54.768), pickupMidPose};
    public static Pose[] pickupMidToTopZonePoses = {pickupMidPose, topZonePose};
    public static Pose[] topZoneToPickupGatePoses = {topZonePose, pickupGatePose};
    public static Pose[] pickupGateToTopZonePoses = {pickupGatePose, topZonePose};
    public static Pose[] topZoneToPickupTopPoses = {topZonePose, new Pose(42.18, 88.597), pickupTopPose};
    public static Pose[] pickupTopToTopZonePoses = {pickupTopPose, topZonePose};
    public static Pose[] topZoneToPickupLowPoses = {topZonePose, new Pose(63.299, 25.934), pickupLowPose};
    public static Pose[] pickupLowToLowZonePoses = {pickupLowPose, lowZonePose};
    public static Pose[] pickupLowToTopZonePoses = {pickupLowPose, new Pose(60, 48), pickupTopPose};
    public static Pose[] lowZoneToLeavePoses = {lowZonePose, leavePose};
}
