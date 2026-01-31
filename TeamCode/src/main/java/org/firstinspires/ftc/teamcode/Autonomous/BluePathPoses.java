package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class BluePathPoses {
    public static Pose StartBluePose = new Pose(58.10526315789475, 7.368421052631579, Math.toRadians(90));
    public static Pose BlueLowZonePose = new Pose(58, 10.263157894736842);
    public static Pose BlueCp1LowZoneToLoadZone =new Pose(63.36842105263157,10.842105263157883);
    public static Pose BlueCp2LowZoneToLoadZone =new Pose(23.157894736842106, 10.947368421052621);
    public static Pose BlueLoadZone = new Pose(6.105263157894737, 10.080568720379151);
    public static Pose BlueLoadZoneRight = new Pose(6.105263157894737, 13.080568720379151);
    public static Pose BlueLoadZoneLeft = new Pose(6.105263157894737, 7.368421052631579);
    public static Pose BlueSemiLoadZone = new Pose(20.03791469194312, 10.080568720379151);
    public static Pose BluePickUpLow = new Pose(18.157894736842092,35.894736842105274);
    public static Pose BlueLeavePose = new Pose(58, 35.631578947368425);

    public static Pose[] BlueStartToLowZonePose = {StartBluePose, BlueLowZonePose};
    public static Pose[] BlueLowZoneToLoadZonePose = {BlueLowZonePose, BlueCp1LowZoneToLoadZone, BlueCp2LowZoneToLoadZone, BlueLoadZone};
    public static Pose[] BlueLoadZoneToSemiLoadZonePose = {BlueLoadZone, BlueSemiLoadZone};
    public static Pose[] BlueSemiLoadZoneToLoadZoneLeft = {BlueSemiLoadZone, BlueLoadZoneLeft};
    public static Pose[] BlueLoadZoneRightToSemiLoadZonePose = {BlueLoadZoneRight, BlueSemiLoadZone};
    public static Pose[] BlueSemiLoadZoneToLoadZoneRight = {BlueSemiLoadZone, BlueLoadZoneRight};
    public static Pose[] BlueLoadZoneLeftToLowZonePose = {BlueLoadZoneLeft, BlueLowZonePose};
    public static Pose[] BlueLowZonePoseToPickUpLow = {BlueLowZonePose, BlueCp1LowZoneToLoadZone, BluePickUpLow};
    public static Pose[] BluePickUpLowToLowZonePose = {BluePickUpLow, BlueLowZonePose};
    public static Pose[] BlueLowZoneToLeavePose = {BlueLowZonePose, BlueLeavePose};


}