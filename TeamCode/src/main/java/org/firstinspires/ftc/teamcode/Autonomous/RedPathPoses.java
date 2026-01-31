package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class RedPathPoses {
    public static Pose StartRedPose = new Pose(81.10526315789475,7.368421052631579, Math.toRadians(90));
    public static Pose RedLowZonePose = new Pose(81.89473684210526, 10.315789473684173);
    public static Pose RedLoadZone = new Pose(132.54028436018956, 10.080568720379151);
    public static Pose RedSemiLoadZone = new Pose(118.03791469194312, 10.080568720379151);
    public static Pose RedLoadZoneLeft = new Pose(132.54028436018956, 13.080568720379151);
    public static Pose RedLoadZoneRight = new Pose(132.54028436018956, 7.368421052631579);
    public static Pose RedPickUpLow = new Pose(115.157894736842092, 44.894736842105274);
    public static Pose RedLeavePose = new Pose(81.89473684210526, 35.42105263157894);
    public static Pose[] RedStartToLowZonePose = {StartRedPose, RedLowZonePose};
    public static Pose[] RedLowZoneToLoadZonePose = {RedLowZonePose, RedLoadZone};
    public static Pose[] RedLoadZoneToSemiLoadZonePose = {RedLoadZone, RedSemiLoadZone};
    public static Pose[] RedSemiLoadZoneToLoadZoneLeft = {RedSemiLoadZone, RedLoadZoneLeft};
    public static Pose[] RedLoadZoneLeftToSemiLoadZonePose = {RedLoadZoneLeft, RedSemiLoadZone};
    public static Pose[] RedSemiLoadZoneToLoadZoneRight = {RedSemiLoadZone, RedLoadZoneRight};
    public static Pose[] RedLoadZoneRightToLowZonePose = {RedLoadZoneRight, RedLowZonePose};
    public static Pose[] RedLowZonePoseToPickUpLowPose = {RedLowZonePose, RedPickUpLow};
    public static Pose[] RedPickUpLowToLowZonePose = {RedPickUpLow, RedLowZonePose};
    public static Pose[] RedLowZoneToLeavePose = {RedLowZonePose, RedLeavePose};
}