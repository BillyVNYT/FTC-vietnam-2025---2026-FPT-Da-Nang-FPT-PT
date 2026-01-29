package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        int t=1;
        MeepMeep meepMeep = new MeepMeep(500);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(14, 16)
                .build();
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-54, 44, Math.toRadians(90)))
                .strafeTo(new Vector2d(-23, 23))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(-13, 37), Math.toRadians(90))

                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(-13, 55), Math.toRadians(90))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(-23, 23), Math.toRadians(90))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(12, 37), Math.toRadians(90))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(12, 53), Math.toRadians(80))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(0, 35), Math.toRadians(90))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(-23, 23), Math.toRadians(180))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(36,40),Math.toRadians(90))
                .lineToYConstantHeading(56)
                .waitSeconds(t)
                .strafeToConstantHeading(new Vector2d(-23,23))
                .build());
        RoadRunnerBotEntity myBot2 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(14, 16)
                .build();
        myBot2.runAction(myBot2.getDrive().actionBuilder(new Pose2d(-54, 44, Math.toRadians(90)))
                .strafeTo(new Vector2d(-23, 23))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(-13, 37), Math.toRadians(90))

                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(-13, 55), Math.toRadians(90))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(-23, 23), Math.toRadians(90))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(12, 37), Math.toRadians(90))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(12, 53), Math.toRadians(80))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(0, 35), Math.toRadians(90))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(-23, 23), Math.toRadians(180))
                .waitSeconds(t)
                .splineToConstantHeading(new Vector2d(36,40),Math.toRadians(90))
                .lineToYConstantHeading(56)
                .waitSeconds(t)
                .strafeToConstantHeading(new Vector2d(-23,23))
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .addEntity(myBot2)
                .start();
    }
}
