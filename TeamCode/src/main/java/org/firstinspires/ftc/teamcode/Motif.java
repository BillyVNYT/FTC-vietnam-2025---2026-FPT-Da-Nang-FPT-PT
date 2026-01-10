package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.LimelightHardware;

import java.util.Arrays;
import java.util.List;

public class Motif {
    List<SortBall.BallColor> obelisk21 = Arrays.asList(SortBall.BallColor.GREEN, SortBall.BallColor.PURPLE, SortBall.BallColor.PURPLE);
    List<SortBall.BallColor> obelisk22 = Arrays.asList(SortBall.BallColor.PURPLE, SortBall.BallColor.GREEN, SortBall.BallColor.PURPLE);
    List<SortBall.BallColor> obelisk23 = Arrays.asList(SortBall.BallColor.PURPLE, SortBall.BallColor.PURPLE, SortBall.BallColor.GREEN);
    private final LimelightHardware limelight;

    public Motif(HardwareMap hardwareMap) {
        limelight = new LimelightHardware(hardwareMap);
    }

    public List<SortBall.BallColor> getMotif() {
        int id = limelight.getAprilTagData().id;
        switch (id) {
            case 21:
                return obelisk21;
            case 22:
                return obelisk22;
            case 23:
                return obelisk23;
            default:
                return null;
        }
    }
}
