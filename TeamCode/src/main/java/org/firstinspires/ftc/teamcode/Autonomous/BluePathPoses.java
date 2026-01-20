package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class BluePathPoses {
    public static Pose topZonePose = new Pose(65.17855664951552, 79.94421324671876);
    public static Pose pickupMidPose = new Pose(9.55, 54.67825604909545);
    public static Pose pickupGatePose = new Pose(3.32985625834459, 64.00580119771699);
    public static Pose pickupTopPose = new Pose(19, 83.943);
    public static Pose pickupLowPose = new Pose(8.530805687203792, 48.0);
    public static Pose lowZonePose = new Pose(59.54502369668247, 8.530805687203792);
    public static Pose leavePose= new Pose(21.32701421800948, 69.44075829383885);

    public static Pose[] topZoneToPickupMidPoses = {topZonePose, new Pose(58.692, 54.768), pickupMidPose};
    public static Pose[] pickupMidToTopZonePoses = {pickupMidPose, topZonePose};
    public static Pose[] topZoneToPickupGatePoses = {topZonePose, pickupGatePose};
    public static Pose[] pickupGateToTopZonePoses = {pickupGatePose, topZonePose};
    public static Pose[] topZoneToPickupTopPoses = {topZonePose, new Pose(42.18, 88.597), pickupTopPose};
    public static Pose[] pickupTopToTopZonePoses = {pickupTopPose, topZonePose};
    public static Pose[] topZoneToPickupLowPoses = {topZonePose, new Pose(63.299, 25.934), pickupLowPose};
    public static Pose[] pickupLowToTopZonePoses = {pickupLowPose, new Pose(60, 48), topZonePose};
    public static Pose[] topZoneToLeavePoses = {topZonePose, leavePose};
}
