package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.List;

public class Motif {
    List<SortBall.BallColor> obelisk21 = Arrays.asList(SortBall.BallColor.GREEN, SortBall.BallColor.PURPLE, SortBall.BallColor.PURPLE);
    List<SortBall.BallColor> obelisk22 = Arrays.asList(SortBall.BallColor.PURPLE, SortBall.BallColor.GREEN, SortBall.BallColor.PURPLE);
    List<SortBall.BallColor> obelisk23 = Arrays.asList(SortBall.BallColor.PURPLE, SortBall.BallColor.PURPLE, SortBall.BallColor.GREEN);

    private final LimelightHardware limelight;
    List<SortBall.BallColor> motif;

    public Motif(HardwareMap hardwareMap) {
        limelight = new LimelightHardware(hardwareMap);
    }

    public List<SortBall.BallColor> getMotif() {
        return motif;
    }

    public void setMotif(Telemetry telemetry) {
        int id = limelight.getAprilTagData(telemetry).id;
        if (id < 21) return;

        switch (id) {
            case 21:
                motif = obelisk21;
                break;
            case 22:
                motif = obelisk22;
                break;
            case 23:
                motif = obelisk23;
                break;
        }
    }
}
