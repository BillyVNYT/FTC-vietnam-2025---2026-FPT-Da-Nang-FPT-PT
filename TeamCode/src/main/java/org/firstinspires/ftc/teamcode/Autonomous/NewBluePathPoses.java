package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class NewBluePathPoses {
    public static Pose StartBluePose = new Pose(58.10526315789475, 7.368421052631579);

    public static Pose BlueLowZonePose = new Pose(61.89473684210526, 26.315789473684173);
    public static Pose BlueCp1LowZoneToLoadZone =new Pose(62.105263157894726,11.052631578947365);
    public static Pose BlueCp2LowZoneToLoadZone =new Pose(19.578947368421055, 4);
    public static Pose BlueLoadZone = new Pose(6.736842105263158, 4.8421052631578725);
    public static Pose BlueLeavePose = new Pose(60.26315789473683, 30.42105263157894);
    public static Pose[] BlueStartToLowZonePose = {StartBluePose, BlueLowZonePose};
    public static Pose[] BlueLowZoneToLoadZonePose = {BlueLowZonePose, BlueCp1LowZoneToLoadZone, BlueCp2LowZoneToLoadZone, BlueLoadZone};
    public static Pose[] BlueLoadZoneReturnToLowZonePose = {BlueLoadZone, BlueLowZonePose};
    public static Pose[] BlueLowZoneToLeavePose = {BlueLowZonePose, BlueLeavePose};


}