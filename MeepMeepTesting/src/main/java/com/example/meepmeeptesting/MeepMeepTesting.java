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
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(64, 8, Math.toRadians(90)))
                .strafeToLinearHeading(new Vector2d(54, 18), Math.toRadians(155))

                .splineToLinearHeading(
                        new Pose2d(62, 35, Math.toRadians(-270)),
                        Math.toRadians(90)
                )
                .lineToY(61)
                .lineToY(35)

                .lineToY(61)
                .lineToY(35)

                .lineToY(61)
                .lineToY(35)

                .splineToLinearHeading(
                        new Pose2d(new Vector2d(53, 18), Math.toRadians(120)),
                        Math.toRadians(90)
                )
                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}