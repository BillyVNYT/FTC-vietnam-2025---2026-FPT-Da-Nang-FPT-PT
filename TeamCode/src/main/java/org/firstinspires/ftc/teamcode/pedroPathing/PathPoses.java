package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.geometry.Pose;

public class PathPoses {
    public final double startHeading;
    public final double endHeading;
    public final Pose[] poses;
    public final GenericAuto.PathState type;

    public PathPoses(double startHeading, double endHeading, Pose[] poses, GenericAuto.PathState type) {
        this.startHeading = startHeading;
        this.endHeading = endHeading;
        this.poses = poses;
        this.type = type;
    }
}
