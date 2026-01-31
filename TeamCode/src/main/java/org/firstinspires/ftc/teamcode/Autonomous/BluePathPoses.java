package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class BluePathPoses {

    public static Pose StartBluePose =
            new Pose(-81.10526315789475, 7.368421052631579,
                    Math.PI - Math.toRadians(90));

    public static Pose BlueLowZonePose =
            new Pose(-81.89473684210526, 10.315789473684173);

    public static Pose BlueLoadZone =
            new Pose(-132.54028436018956, 10.080568720379151);

    public static Pose BlueSemiLoadZone =
            new Pose(-125.03791469194312, 10.080568720379151);

    public static Pose BlueLoadZoneLeft =
            new Pose(-132.54028436018956, 13.080568720379151);

    public static Pose BlueLoadZoneRight =
            new Pose(-132.54028436018956, 7.368421052631579);

    public static Pose BlueCpPickup =
            new Pose(-86.21376901970568, 40.27488151658768);

    public static Pose BluePickUpLow =
            new Pose(-132.54028436018956, 36.322274881516584);

    public static Pose BlueLeavePose =
            new Pose(-81.89473684210526, 37.42105263157894);

    // ---------- Pose arrays ----------
    public static Pose[] BlueStartToLowZonePose =
            {StartBluePose, BlueLowZonePose};

    public static Pose[] BlueLowZoneToLoadZonePose =
            {BlueLowZonePose, BlueLoadZone};

    public static Pose[] BlueLoadZoneToSemiLoadZonePose =
            {BlueLoadZone, BlueSemiLoadZone};

    public static Pose[] BlueSemiLoadZoneToLoadZoneLeft =
            {BlueSemiLoadZone, BlueLoadZoneLeft};

    public static Pose[] BlueLoadZoneRightToSemiLoadZonePose =
            {BlueLoadZoneLeft, BlueSemiLoadZone};

    public static Pose[] BlueSemiLoadZoneToLoadZoneRight =
            {BlueSemiLoadZone, BlueLoadZoneRight};

    public static Pose[] BlueLoadZoneLeftToLowZonePose =
            {BlueLoadZoneLeft, BlueLowZonePose};

    public static Pose[] BlueLowZonePoseToPickUpLow =
            {BlueLowZonePose, BlueCpPickup, BluePickUpLow};

    public static Pose[] BluePickUpLowToLowZonePose =
            {BluePickUpLow, BlueLowZonePose};

    public static Pose[] BlueLowZoneToLeavePose =
            {BlueLowZonePose, BlueLeavePose};
}
