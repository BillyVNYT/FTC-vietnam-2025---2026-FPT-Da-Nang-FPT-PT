package org.firstinspires.ftc.teamcode.Autonomous;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;

public class PathPoses {
    public final double startHeading;
    public double endHeading = -1;
    public final Pose[] poses;
    public final GenericAuto.PathState type;


    public PathPoses(double startHeading, double endHeading, Pose[] poses, GenericAuto.PathState type) {
        this.startHeading = startHeading;
        this.endHeading = endHeading;
        this.poses = poses;
        this.type = type;
    }

    public PathPoses(double startHeading, Pose[] poses, GenericAuto.PathState type) {
        this.startHeading = startHeading;
        this.poses = poses;
        this.type = type;
    }

    public PathPoses setPathConstraints(PathConstraints fastConstraints) {
        return this;
    }
}
