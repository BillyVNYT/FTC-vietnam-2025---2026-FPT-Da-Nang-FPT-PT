package org.firstinspires.ftc.teamcode.utils;
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortBall {
    public enum BallColor {
        GREEN,
        PURPLE,
        EMPTY
    }
    public double[] INTAKE_SLOT_POS = {0.2467, 0.1194, 0};
    public double[] INTAKE_SLOT_POS2 = {0.0711, 0.1772, 0.3006};
    public double[] OUTTAKE_SLOT_POS = {0.0865 , 0.2106, 0.333, 0.4544, 0.7072, 0.8339, 0.9528};
    int bestSpin = 0;
    public boolean is_lastBall = false;

    private final List<BallColor> currentLoad = new ArrayList<>();
    List<BallColor> obeliskData;
    ColorSensor colorSensor1, colorSensor2, colorSensor3, colorSensor4;
//    ColorSensor colorSensor3, colorSensor4;
    Servo spindexer1, spindexer2;
    boolean spindexerReversed = false;

    ElapsedTime timeIntake = new ElapsedTime();
    double nextSlot = INTAKE_SLOT_POS[0], nextSlot2 = INTAKE_SLOT_POS2[0];
    int baseClearCS1, baseClearCS2;
    Shooter shooter;
    DistanceSensor dis1, dis2;
    public SortBall(List<BallColor> obeliskData, HardwareMap hardwareMap, Shooter shooter) {
        this.obeliskData = obeliskData;
        this.shooter = shooter;
        colorSensor1 = hardwareMap.get(ColorSensor.class, "cs");
        colorSensor2 = hardwareMap.get(ColorSensor.class, "cs2");
        colorSensor3 = hardwareMap.get(ColorSensor.class, "cs3");
        colorSensor4 = hardwareMap.get(ColorSensor.class, "cs4");

        dis1 = hardwareMap.get(DistanceSensor.class, "ds");
        dis2 = hardwareMap.get(DistanceSensor.class, "ds2");

        spindexer1 = hardwareMap.get(Servo.class, "s0");
        spindexer2 = hardwareMap.get(Servo.class, "s1");
        spindexer2.setDirection(Servo.Direction.REVERSE);

        spindexer1.setPosition(INTAKE_SLOT_POS[0]);
        spindexer2.setPosition(INTAKE_SLOT_POS[0]);

        currentLoad.add(BallColor.EMPTY);
        currentLoad.add(BallColor.EMPTY);
        currentLoad.add(BallColor.EMPTY);
        baseClearCS1 = colorSensor1.getClear();
        baseClearCS2 = colorSensor2.getClear();
    }


    public List<BallColor> getCurrentLoad() {
        return currentLoad;
    }

    private void releaseBall(int idx) {
        currentLoad.set(idx, BallColor.EMPTY);
    }

    public void readyToShoot(boolean sort, Telemetry telemetry) {
        if (!sort) {
            controlSpindexer(OUTTAKE_SLOT_POS[0]);
            shooter.setMotorVelocity(1300, telemetry);
        } else {
            List<BallColor> reversedLoad = currentLoad.subList(0, 3);
            Collections.reverse(reversedLoad);
            bestSpin = getBestSpin(reversedLoad, telemetry);
            controlSpindexer(bestSpin);
        }
    }

    private int getFirstEmptySlot() {
        for (int i = 0; i < currentLoad.size(); i++) {
            if (currentLoad.get(i) == BallColor.EMPTY) {
                return i;
            }
        }
        return -1;
    }

    private void handleSensor(Telemetry telemetry, BallColor color1, BallColor color2, boolean reversed, DistanceSensor distance) throws InterruptedException{
        telemetry.addData("distance", distance.getDistance(DistanceUnit.MM));
        if(distance.getDistance(DistanceUnit.MM) < 150) {
            int firstEmptyIdx = getFirstEmptySlot();

            telemetry.addData("Empty slot", firstEmptyIdx);

            if (firstEmptyIdx < 0) return;

            boolean cs1Detected = !color1.equals(BallColor.EMPTY);
            boolean cs2Detected = !color2.equals(BallColor.EMPTY);
            int delta1 = colorSensor1.getClear() - baseClearCS1;
            int delta2 = colorSensor2.getClear() - baseClearCS2;
            BallColor color = cs1Detected ? color1 : color2;
            BallColor chosenColor = BallColor.EMPTY;

            if (cs1Detected && !cs2Detected) {
                chosenColor = color1;
            } else if (cs2Detected && !cs1Detected) {
                chosenColor = color2;
            } else if (cs1Detected && cs2Detected) {
                if (delta1 > delta2 && delta1 > 2000) {
                    chosenColor = color1;
                } else if (delta2 > delta1 && delta2 > 1500) {
                    chosenColor = color2;
                }
            }

            if (chosenColor != BallColor.EMPTY) {
                currentLoad.set(firstEmptyIdx, chosenColor);

                if (firstEmptyIdx < 2) {
                    nextSlot = INTAKE_SLOT_POS[firstEmptyIdx + 1];
                    nextSlot2 = INTAKE_SLOT_POS2[firstEmptyIdx + 1];
                    controlSpindexer(reversed ? nextSlot2 : nextSlot);
                } else {
                    telemetry.addLine("ALL IN");
                    telemetry.update();
                    readyToShoot(false, telemetry);
                }
            }
        }
    }

    public void autoLoadBallsIn(Telemetry telemetry) throws InterruptedException {
        BallColor colorFront1 = colorSensor1.detectBallColor(2000, telemetry);
        BallColor colorFront2 = colorSensor2.detectBallColor(2200, telemetry);
        handleSensor(telemetry, colorFront1, colorFront2, false, dis1);
    }

    public void loadBallsIn(Telemetry telemetry, Gamepad gamepad) throws InterruptedException {
        if(!spindexerReversed && timeIntake.seconds() > 0.5) {
            BallColor colorFront1 = colorSensor1.detectBallColor(2200, telemetry);
            BallColor colorFront2 = colorSensor2.detectBallColor(2200, telemetry);
            handleSensor(telemetry, colorFront1, colorFront2, false, dis1);
        }

        if(spindexerReversed && timeIntake.seconds() > 0.5) {
            BallColor colorTail3 = colorSensor3.detectBallColor(2200, telemetry);
            BallColor colorTail4 = colorSensor4.detectBallColor(2200, telemetry);
            handleSensor(telemetry, colorTail3, colorTail4, true, dis2);
        }

        if(gamepad.left_stick_x < 0 && spindexerReversed) {
            // chuyển hướng đi tiến
            spindexerReversed = false;
            controlSpindexer(nextSlot);
            timeIntake.reset();
        }

        if(gamepad.left_stick_x > 0 && !spindexerReversed){
            // chuyển hướng đi lùi
            spindexerReversed = true;
            controlSpindexer(nextSlot2);
            timeIntake.reset();
        }
    }

    public boolean isFull(){
        return !currentLoad.contains(BallColor.EMPTY);
    }

    /**
     * @param currentLoad  Array of 3 strings (e.g., {"G", "P", "E"})
     * @return int The number of spins needed (0, 1, or 2).
     */
    public int getBestSpin(List<BallColor> currentLoad, Telemetry telemetry) {
        int bestSpin = 0;
        int maxMatches = -1;

        for (int spins = 0; spins < 3; spins++) {
            int currentMatches = getCurrentMatches(currentLoad, spins);
            if (currentMatches > maxMatches) {
                maxMatches = currentMatches;
                bestSpin = spins;

            }
        }
        telemetry.addData("Intake:", currentLoad);
        telemetry.addData("Best Spin:", bestSpin);
        return bestSpin;
    }

    private int getCurrentMatches(List<BallColor> currentLoad, int spins) {
        List<BallColor> firingStream = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            // (i + spins) % 3 simulates the rotation index
            BallColor bullet = currentLoad.get((i + spins) % 3);
            if (bullet != BallColor.EMPTY) firingStream.add(bullet);
        }

        int currentMatches = 0;
        int checkLimit = Math.min(firingStream.size(), obeliskData.size());

        for (int k = 0; k < checkLimit; k++) {
            if (firingStream.get(k).equals(obeliskData.get(k))) {
                currentMatches++;
            }
        }
        return currentMatches;
    }


    public void spinTargetToShooter(BallColor target){
        for (int i = 0; i < currentLoad.size(); i++) {
            if (currentLoad.get(i) == target) {
                controlSpindexer(OUTTAKE_SLOT_POS[i]);
                break;
            }
        }
    }

    public void spinToShooter(int count, Telemetry telemetry) throws InterruptedException{
        releaseBall(count-1);
        sleep(360);

        for(int i = 1; i < count + 1; i++) { // xoay them 1 vi tri de ban qua cuoi cung
            controlSpindexer(OUTTAKE_SLOT_POS[bestSpin + i]);
            if (i < count) releaseBall(count-1-i);
            if(i == count){
                is_lastBall = true;
            }
            sleep(500);
        }

        // spin to next empty slot
        int firstEmptyIdx = getFirstEmptySlot();
        telemetry.addData("firstEmptyIdx",firstEmptyIdx);
        telemetry.update();

        nextSlot = INTAKE_SLOT_POS[firstEmptyIdx];
        nextSlot2 = INTAKE_SLOT_POS2[firstEmptyIdx];
        controlSpindexer(spindexerReversed ? nextSlot2 : nextSlot);
    }
    public void controlSpindexer(double position){
        spindexer1.setPosition(position);
        spindexer2.setPosition(position);
    }
}
