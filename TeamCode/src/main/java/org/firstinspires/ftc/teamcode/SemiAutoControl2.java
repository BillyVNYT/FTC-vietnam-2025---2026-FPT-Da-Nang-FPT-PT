package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.Intake;
import org.firstinspires.ftc.teamcode.utils.Shooter;
import org.firstinspires.ftc.teamcode.utils.Lifter;
import org.firstinspires.ftc.teamcode.utils.SortBall;

import java.util.ArrayList;
import java.util.List;


public class SemiAutoControl2 {
    Shooter shooter;
    Lifter lifter;
    SortBall spindexer;
    Gamepad Gamepad1;
    Intake intake;
    List<SortBall.BallColor> samples;


    public SemiAutoControl2(HardwareMap hardwareMap) {
        lifter = new Lifter(hardwareMap);
        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);

        samples = new ArrayList<>();
        samples.add(SortBall.BallColor.PURPLE);
        samples.add(SortBall.BallColor.PURPLE);
        samples.add(SortBall.BallColor.GREEN);

        spindexer = new SortBall(samples, hardwareMap);
    }

    public void controlLifter() {
        if (Gamepad1.dpadUpWasPressed()) {
            lifter.lift();
        } else if (Gamepad1.dpadDownWasPressed()) {
            lifter.lower();
        }
    }


}