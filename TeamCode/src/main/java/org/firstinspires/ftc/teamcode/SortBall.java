package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class SortBall {
    Servo SortBall;
    ColorSensor slot1;
    ColorSensor slot2;
    ColorSensor slot3;
    private AutoTrackStartMatch autoTrackStartMatch;
    public SortBall(HardwareMap hardwareMap){
        autoTrackStartMatch = new AutoTrackStartMatch(hardwareMap);
        SortBall = hardwareMap.get(Servo.class, "s3");
    }
    public void checkSlot(){
        if(checkBall(slot1) == 0){

        }
    }
    public int checkBall(ColorSensor colorSensor){
        if(colorSensor.blue() > colorSensor.red()){
            return 0;
        } else {
            return 1;
        }
    }
}
