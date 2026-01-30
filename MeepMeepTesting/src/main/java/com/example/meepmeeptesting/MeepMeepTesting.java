package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        int t=0;
        MeepMeep meepMeep = new MeepMeep(500);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(14, 16)
                .build();
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-54, 44, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(-15, 15), Math.toRadians(135))
                .splineToLinearHeading(new Pose2d(14, 18, Math.toRadians(90)), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(14, 59), Math.toRadians(80), new TranslationalVelConstraint(30))
                .splineToConstantHeading(new Vector2d(12, 45), Math.toRadians(90), new TranslationalVelConstraint(20))
                .splineToConstantHeading(new Vector2d(5, 52), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(-13, 35, Math.toRadians(90)), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(-13, 55), Math.toRadians(90), new TranslationalVelConstraint(30))
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(36,32, Math.toRadians(90)),Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(36, 62), Math.toRadians(90), new TranslationalVelConstraint(30))
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.toRadians(90))
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}