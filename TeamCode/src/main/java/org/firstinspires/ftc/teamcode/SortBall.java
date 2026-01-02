package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class SortBall {
    Servo SortBall;
    ColorSensor
    private AutoTrackStartMatch autoTrackStartMatch;
    public SortBall(HardwareMap hardwareMap){
        autoTrackStartMatch = new AutoTrackStartMatch(hardwareMap);
        SortBall = hardwareMap.get(Servo.class, "s3");
    }
}
