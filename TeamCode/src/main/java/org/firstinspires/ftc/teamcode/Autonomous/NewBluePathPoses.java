package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class NewBluePathPoses {
    public static Pose StartBluePose = new Pose(58.10526315789475, 7.368421052631579);

    public static Pose BlueLowZonePose = new Pose(61.89473684210526, 26.315789473684173);
    public static Pose BlueCp1LowZoneToLoadZone =new Pose(63.36842105263157,19.263157894736832);
    public static Pose BlueCp2LowZoneToLoadZone =new Pose(23.157894736842106, 7.789473684210522);
    public static Pose BlueLoadZone = new Pose(6.105263157894737, 7.578947368421028);
    public static Pose BluePickUpLow = new Pose(18.157894736842092,35.894736842105274);
    public static Pose BlueCp1PickUpLow = new Pose(62.68421052631577,35.578947368421055);
    public static Pose BlueLeavePose = new Pose(62.6842105263158, 35.631578947368425);



    public static Pose[] BlueStartToLowZonePose = {StartBluePose, BlueLowZonePose};
    public static Pose[] BlueLowZoneToLoadZonePose = {BlueLowZonePose, BlueCp1LowZoneToLoadZone, BlueCp2LowZoneToLoadZone, BlueLoadZone};
    public static Pose[] BlueLoadZoneReturnToLowZonePose = {BlueLoadZone, BlueLowZonePose};
    public static Pose[] BlueLowZonePoseToPicklUpLow = {BlueLowZonePose, BlueCp1LowZoneToLoadZone, BluePickUpLow};
    public static Pose[] BluePickUpLowToLeavePose = {BluePickUpLow, BlueLeavePose};

    public static Pose[] BlueLowZoneToLeavePose = {BlueLowZonePose, BlueLowZonePose};


}