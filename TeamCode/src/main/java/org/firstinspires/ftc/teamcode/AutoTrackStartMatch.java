package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Arrays;
import java.util.List;

class ObeliskData{
    List<Integer> obelisk1 = Arrays.asList(1, 0, 0);
    List<Integer> obelisk2 = Arrays.asList(0, 1, 0);
    List<Integer> obelisk3 = Arrays.asList(0, 0, 1);
    @SuppressLint("NotConstructor")
    List<Integer> ObeliskData(int id){
        if(id == 21) {
            return obelisk1;
        } else if(id == 22){
            return obelisk2;
        } else if(id == 23){
            return obelisk3;
        }
        return obelisk1;
    }
}

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
