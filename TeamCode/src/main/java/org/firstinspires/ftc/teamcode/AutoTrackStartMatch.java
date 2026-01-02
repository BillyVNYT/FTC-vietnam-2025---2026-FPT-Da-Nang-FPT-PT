package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.TeleOp.MainBlue;
import org.firstinspires.ftc.teamcode.TeleOp.MainRed;

public class AutoTrackStartMatch {
    public int ObeliskNum = 36;
    private LimelightHardware limelightHardware;
    public AutoTrackStartMatch(HardwareMap hardwareMap){
        limelightHardware = new LimelightHardware(hardwareMap);
    }
    public void AutoTrackObelisk(){
        ObeliskNum = limelightHardware.getAprilTagData().id;
    }
}
