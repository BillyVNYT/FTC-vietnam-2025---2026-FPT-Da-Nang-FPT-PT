package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.geometry.Pose;

public class PathPoses {
    Pose[] poses;
    GenericAuto.PathState type;
    double startHeading, endHeading;

    public PathPoses(double startHeading, double endHeading, Pose[] poses, GenericAuto.PathState type) {
        this.startHeading = startHeading;
        this.endHeading = endHeading;
        this.poses = poses;
        this.type = type;
    }
}
