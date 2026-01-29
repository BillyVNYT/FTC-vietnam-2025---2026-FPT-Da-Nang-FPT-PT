package org.firstinspires.ftc.teamcode.Autonomous;

import com.pedropathing.geometry.Pose;

public class NewBluePathPoses {
    public static Pose StartBluePose = new Pose(27.294892915980228, 127.09060955518947);
    public static Pose BlueTopZonePose = new Pose(48.171334431630974,96.73146622734761);
    public static Pose PickupMidBluePose = new Pose(9.473684210526315, 59.99999999999999);
    public static Pose PickupMidCp1 = new Pose(49.502471169686984,54.822075782537084);
    public static Pose PickupMidCp2 = new Pose(51.57001647446457,61.205930807248784);
    public static Pose Backward = new Pose(23.664230739329845, 56.86426183627462);
    public static Pose BlueGatePose = new Pose(10.631578947368423,68.68421052631577);
    public static Pose PickupTopBluePose = new Pose(10.257521893696351,84.2174629324547);

    public static Pose PickupTopCp1 = new Pose(51.97693574958814,80.59637561779243);
    public static Pose PickupTopCp2 = new Pose(41.55683690280065,83.9802306425041);

    public static Pose PickupLowBluePose = new Pose(7.116968698517289,35.26688632619441);

    public static Pose PickupLowCp1 = new Pose(47.97858319604613,34.7009884678748);
    public static Pose PickupLowCp2 = new Pose(48.92751235584846,35.17545304777596);
    public static Pose PickupLowCp3 = new Pose(48.655683690280064, 48.655683690280064);
    public static Pose[] StartToShoot = {StartBluePose, BlueTopZonePose};
    public static Pose[] TopZoneToPickupMid = {BlueTopZonePose, PickupMidCp1, PickupMidCp2,PickupMidBluePose};
    public static Pose[] PickupMidToBackward = {PickupMidBluePose, Backward};
    public static Pose[] BackwardToBlueGate = {Backward,BlueGatePose};
    public static Pose[] BlueGateToTopZone = {BlueGatePose,PickupMidCp2,PickupMidCp1,BlueTopZonePose};
    public static Pose[] TopZoneToPickUpTop = {BlueTopZonePose, PickupTopCp1, PickupTopCp2,PickupTopBluePose};
    public static Pose[] PickUpTopToTopZone = {PickupTopBluePose,PickupTopCp2,PickupTopCp1,BlueTopZonePose};
    public static Pose[] TopZoneToPickUpLow = {BlueTopZonePose, PickupLowCp1, PickupLowCp2,PickupLowCp3,PickupLowBluePose};
    public static Pose[] PickUpLowToTopZone = {PickupLowBluePose,PickupLowCp3,PickupTopCp2,PickupLowCp1,BlueTopZonePose};






}