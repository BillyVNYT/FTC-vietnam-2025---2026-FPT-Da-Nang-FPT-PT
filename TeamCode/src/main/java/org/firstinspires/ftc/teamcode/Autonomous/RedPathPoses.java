package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class RedPathPoses {
    public static Pose StartRedPose = new Pose(81.10526315789475,7.368421052631579, Math.toRadians(90));
    public static Pose RedLowZonePose = new Pose(81.89473684210526, 10.315789473684173);
    public static Pose RedCp1LowZoneToLoadZone =new Pose(83.15789473684208,10.842105263157883);
    public static Pose RedCp2LowZoneToLoadZone =new Pose(106.73684210526315, 10.947368421052621);
    public static Pose RedLoadZone = new Pose(132.54028436018956, 10.080568720379151);
    public static Pose RedSemiLoadZone = new Pose(118.03791469194312, 10.080568720379151);
    public static Pose RedLeavePose = new Pose(115.26315789473683, 10.42105263157894);
    public static Pose[] RedStartToLowZonePose = {StartRedPose, RedLowZonePose};
    public static Pose[] RedLowZoneToLoadZonePose = {RedLowZonePose, RedCp1LowZoneToLoadZone, RedCp2LowZoneToLoadZone, RedLoadZone};
    public static Pose[] RedLoadZoneReturnToLowZonePose = {RedLoadZone, RedLowZonePose};
    public static Pose[] RedLoadZoneToSemiLoadZonePose = {RedLoadZone, RedSemiLoadZone};
    public static Pose[] RedSemiLoadZoneToLoadZonePose = {RedSemiLoadZone, RedLoadZone};
    public static Pose[] RedLowZoneToLeavePose = {RedLowZonePose, RedLeavePose};
}
