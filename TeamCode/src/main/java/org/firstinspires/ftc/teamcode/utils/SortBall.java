package org.firstinspires.ftc.teamcode.utils;
import static java.lang.Thread.sleep;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortBall {
    public enum BallColor {
        GREEN,
        PURPLE,
        EMPTY
    }
    double[] INTAKE_SLOT_POS = {0.2467, 0.1194, 0};
    double[] OUTTAKE_SLOT_POS = {0.0878, 0.2106, 0.3289, 0.7072, 0.8339, 0.9528};
    int ID_Obelisk = 23;

    private final List<SortBall.BallColor> currentLoad = new ArrayList<>();
    List<BallColor> obeliskData;
    ColorSensor colorSensor1, colorSensor2;
    Servo spindexer1, spindexer2;
    BallColor FistBall, SecondBall;
    public SortBall(List<BallColor> obeliskData, HardwareMap hardwareMap) {
        this.obeliskData = obeliskData;
        colorSensor1 = hardwareMap.get(ColorSensor.class, "cs");
        colorSensor2 = hardwareMap.get(ColorSensor.class, "cs2");

        spindexer1 = hardwareMap.get(Servo.class, "s0");
        spindexer2 = hardwareMap.get(Servo.class, "s1");
        spindexer2.setDirection(Servo.Direction.REVERSE);

        spindexer1.setPosition(INTAKE_SLOT_POS[0]);
        spindexer2.setPosition(INTAKE_SLOT_POS[0]);

        currentLoad.add(BallColor.EMPTY);
        currentLoad.add(BallColor.EMPTY);
        currentLoad.add(BallColor.EMPTY);
    }


    public List<SortBall.BallColor> getCurrentLoad() {
        return currentLoad;
    }

    private void releaseBall(int idx) {
        currentLoad.set(idx, BallColor.EMPTY);
    }

    public void readyToShoot() {
        currentLoad.addAll(new ArrayList<>(currentLoad.subList(0, 3)));
        if(ID_Obelisk == 23){
            FistBall = BallColor.PURPLE;
            SecondBall = BallColor.PURPLE;
        } else if (ID_Obelisk == 22) {
            FistBall = BallColor.PURPLE;
            SecondBall = BallColor.GREEN;
        } else {
            FistBall = BallColor.GREEN;
            SecondBall = BallColor.PURPLE;
        }
        for(int i = 1; i < 6; i++){
            if(currentLoad.get(i) == SecondBall && currentLoad.get(i-1) == FistBall){
                controlSpindexer(OUTTAKE_SLOT_POS[i-1]);
            }
        }
    }

    public void loadBallsIn(Telemetry telemetry) throws InterruptedException {
        BallColor color1 = colorSensor1.detectBallColor(1500, telemetry);
        BallColor color2 = colorSensor2.detectBallColor(4000, telemetry);
//        BallColor color3 = colorSensor3.detectBallColor(4000, telemetry);
        int firstEmptyIdx = -1;
        for (int i = 0; i < currentLoad.size(); i++) {
            if (currentLoad.get(i) == BallColor.EMPTY) {
                firstEmptyIdx = i;
                break;
            }
        }

        telemetry.addData("Empty slot", firstEmptyIdx);
        telemetry.update();

        boolean cs1Detected = !color1.equals(BallColor.EMPTY);
        boolean cs2Detected = !color2.equals(BallColor.EMPTY);
//        boolean cs3Detected = !color3.equals(BallColor.EMPTY);
        BallColor color = cs1Detected ? color1 : color2;

        if((cs1Detected || cs2Detected) && firstEmptyIdx > -1) {
            currentLoad.set(firstEmptyIdx, color);
            if(firstEmptyIdx < 2) {
                // spin to next empty slot
                double nextSlot = INTAKE_SLOT_POS[firstEmptyIdx + 1];
                controlSpindexer(nextSlot);

                long sleepTime = (long) (Math.abs(nextSlot - INTAKE_SLOT_POS[firstEmptyIdx])*700);
                sleep(250);
            } else readyToShoot();
        }
    }

    public boolean isFull(){
        for (BallColor b : currentLoad) {
            if (b == BallColor.EMPTY) return false;
        }
        return true;
    }

    /**
     * @param currentLoad  Array of 3 strings (e.g., {"G", "P", "E"})
     * @return int The number of spins needed (0, 1, or 2).
     */
    public int getBestSpin(BallColor[] currentLoad, TelemetryManager telemetry) {
        int bestSpin = 0;
        int maxMatches = -1;

        for (int spins = 0; spins < 3; spins++) {
            int currentMatches = getCurrentMatches(currentLoad, spins);
            if (currentMatches > maxMatches) {
                maxMatches = currentMatches;
                bestSpin = spins;

            }
        }
        telemetry.addData("Intake:", Arrays.toString(currentLoad));
        telemetry.addData("Best Spin:", bestSpin);
        return bestSpin;
    }

    private int getCurrentMatches(BallColor[] currentLoad, int spins) {
        List<BallColor> firingStream = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            // (i + spins) % 3 simulates the rotation index
            BallColor bullet = currentLoad[(i + spins) % 3];
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


    public void spinTargetToShooter(SortBall.BallColor target){
        for (int i = 0; i < currentLoad.size(); i++) {
            if (currentLoad.get(i) == target) {
//                spindexer.setPosition(OUTTAKE_SLOT_POS[i]);
                break;
            }
        }
    }

    public void spinToShooter(int count) throws InterruptedException{
        releaseBall(0);
//        spindexer.setDirection(Servo.Direction.REVERSE);
        sleep(360);

        for(int i = 1; i < count; i++) {
//            spindexer.setPosition(OUTTAKE_SLOT_POS[i]);
            releaseBall(i);
            sleep(600);
        }

        sleep(300);
    }
    public void controlSpindexer(double position){
        spindexer1.setPosition(position);
        spindexer2.setPosition(position);
    }
}
