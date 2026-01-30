package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class NewRedPathPoses {
    public static Pose StartRedPose = new Pose(78.10526315789475,7.368421052631579, Math.toRadians(90));
    public static Pose RedLowZonePose = new Pose(81.89473684210526, 16.315789473684173);
    public static Pose RedCp1LowZoneToLoadZone =new Pose(83.15789473684208,10.842105263157883);
    public static Pose RedCp2LowZoneToLoadZone =new Pose(106.73684210526315, 10.947368421052621);
    public static Pose RedLoadZone = new Pose(125.54028436018956, 6.080568720379151);
    public static Pose RedSemiLoadZone = new Pose(108.03791469194312, 6.080568720379151);
    public static Pose RedPickUpLow = new Pose(126.36842105263156,35.26315789473685);
    public static Pose RedCp1PickUpLow = new Pose(83.73684210526312, 37.05263157894734);
    public static Pose RedLeavePose = new Pose(80.26315789473683, 30.42105263157894);
    public static Pose[] RedStartToLowZonePose = {StartRedPose, RedLowZonePose};
    public static Pose[] RedLowZoneToLoadZonePose = {RedLowZonePose, RedCp1LowZoneToLoadZone, RedCp2LowZoneToLoadZone, RedLoadZone};
    public static Pose[] RedLoadZoneReturnToLowZonePose = {RedLoadZone, RedLowZonePose};
    public static Pose[] RedLoadZoneToSemiLoadZonePose = {RedLoadZone, RedSemiLoadZone};
    public static Pose[] RedSemiLoadZoneToLoadZonePose = {RedSemiLoadZone, RedLoadZone};
    public static Pose[] RedLoadZoneToLowZonePose = {RedLoadZone,RedLowZonePose};
    public static Pose[] RedLowZonePoseToPickUpLowPose = {RedLowZonePose, RedPickUpLow};
    public static Pose[] RedPickUpLowToLowZonePose = {RedPickUpLow, RedLowZonePose};
    public static Pose[] RedLowZoneToLeavePose = {RedLowZonePose, RedLeavePose};
}
