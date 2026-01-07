package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class BluePathPoses {
    public static Pose topZonePose = new Pose(63.810, 70.294);
    public static Pose pickupMidPose = new Pose(8.531, 59.204);
    public static Pose pickupGatePose = new Pose(11.261, 60.057);
    public static Pose pickupTopPose = new Pose(13.649, 83.943);
    public static Pose pickupLowPose = new Pose(8.531, 36.0);
    public static Pose lowZonePose = new Pose(59.545, 18.531);
    public static Pose leavePose= new Pose(21.327, 69.441);

    public static Pose[] topZoneToPickupMidPoses = {topZonePose, new Pose(58.692, 54.768), pickupMidPose};
    public static Pose[] pickupMidToTopZonePoses = {pickupMidPose, topZonePose};
    public static Pose[] topZoneToPickupGatePoses = {topZonePose, pickupGatePose};
    public static Pose[] pickupGateToTopZonePoses = {pickupGatePose, topZonePose};
    public static Pose[] topZoneToPickupTopPoses = {topZonePose, new Pose(42.18, 88.597), pickupTopPose};
    public static Pose[] pickupTopToTopZonePoses = {pickupTopPose, topZonePose};
    public static Pose[] topZoneToPickupLowPoses = {topZonePose, new Pose(63.299, 25.934), pickupLowPose};
    public static Pose[] pickupLowToLowZonePoses = {pickupLowPose, lowZonePose};
    public static Pose[] lowZoneToLeavePoses = {lowZonePose, leavePose};
}
