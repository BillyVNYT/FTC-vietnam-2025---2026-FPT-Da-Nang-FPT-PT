package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class BluePathPoses {
    public static Pose topZonePose = new Pose(58.17855664951552, 83.94421324671876);
    public static Pose pickupMidPose = new Pose(8.4, 57.67825604909545);
    public static Pose openGatePose = new Pose(10.122, 63.00580119771699);
    public static Pose pickupTopPose = new Pose(16.335, 84.133);
    public static Pose pickupLowPose = new Pose(8.030805687203792, 52.0);
    public static Pose lowZonePose = new Pose(59.54502369668247, 8.530805687203792);
    public static Pose loadingZonePose = new Pose(9.59241706161137,8.360189573459726);
    public static Pose leavePose= new Pose(21.32701421800948, 69.44075829383885);

    public static Pose[] topZoneToPickupMidPoses = {topZonePose, new Pose(49.502, 54.822),
            new Pose(51.570, 61.206), pickupMidPose};
    public static Pose[] pickupMidToOpenGatePoses = {pickupMidPose, new Pose(27.155, 54.552), openGatePose};
    public static Pose[] pickupMidToTopZonePoses = {pickupMidPose, topZonePose};
    public static Pose[] openGateToTopZonePoses = {openGatePose, topZonePose};
    public static Pose[] topZoneToPickupTopPoses = {topZonePose, new Pose(42.18, 88.597), pickupTopPose};
    public static Pose[] pickupTopToTopZonePoses = {pickupTopPose, topZonePose};
    public static Pose[] topZoneToPickupLowPoses = {topZonePose, new Pose(63.299, 25.934), pickupLowPose};
    public static Pose[] pickupLowToTopZonePoses = {pickupLowPose, new Pose(60, 48), topZonePose};
    public static Pose[] topZoneToLeavePoses = {topZonePose, leavePose};
}