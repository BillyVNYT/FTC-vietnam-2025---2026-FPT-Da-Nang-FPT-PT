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
    double[] INTAKE_SLOT_POS = {0.7424, 0.3688, 0};
    double[] OUTTAKE_SLOT_POS = {0.328, 0.5, 0.6};

    List<BallColor> obeliskData;
    ColorSensor colorSensor1, colorSensor2, colorSensor3;
    Servo spindexer;

    public SortBall(List<BallColor> obeliskData, HardwareMap hardwareMap) {
        this.obeliskData = obeliskData;
        colorSensor1 = hardwareMap.get(ColorSensor.class, "cs1");
        colorSensor2 = hardwareMap.get(ColorSensor.class, "cs2");
        colorSensor3 = hardwareMap.get(ColorSensor.class, "cs3");
        spindexer = hardwareMap.get(Servo.class, "s1");

        spindexer.setPosition(INTAKE_SLOT_POS[0]);
    }

    private final List<SortBall.BallColor> currentLoad = new ArrayList<>();

    public List<SortBall.BallColor> getCurrentLoad() {
        return currentLoad;
    }

    private void releaseBall() {
        currentLoad.remove(0);
    }

//    public void readyToShoot() {
//        spindexer.setPosition(OUTTAKE_SLOT_POS[0]);
//    }

    int MAX_SIZE = 300;

    public void loadBallsIn(Telemetry telemetry) throws InterruptedException {
        BallColor color1 = colorSensor1.detectBallColor(2500);
        BallColor color2 = colorSensor2.detectBallColor(4000);
//        BallColor color3 = colorSensor3.detectBallColor(5000);

        int size = currentLoad.size();
        telemetry.addData("Size", size);

        if(size == 4) {
            telemetry.addLine("-----------ALL IN!!!");
            return;
        }

        boolean cs1Detected = !color1.equals(BallColor.EMPTY);
        boolean cs2Detected = !color2.equals(BallColor.EMPTY);
//        boolean cs3Detected = !color3.equals(BallColor.EMPTY);
        BallColor color = cs1Detected ? color1 : color2;

        if((cs1Detected || cs2Detected) && size < MAX_SIZE) {
            currentLoad.add(color);
//             spin to next empty slot
            sleep(5000);
        }
        spindexer.setPosition(INTAKE_SLOT_POS[size]);

        String cs1Data = "r:" + colorSensor1.getRed() + " g:" + colorSensor1.getGreen() + " c:" + colorSensor1.getClear();
        String cs2Data = "r:" + colorSensor2.getRed() + " g:" + colorSensor2.getGreen() + " c:" + colorSensor2.getClear();
        String cs3Data = "r:" + colorSensor3.getRed() + " g:" + colorSensor3.getGreen() + " c:" + colorSensor3.getClear();
        telemetry.addData("cs1 detected", color1);
        telemetry.addData("cs1 data", cs1Data);
        telemetry.addData("cs2 detected", color2);
        telemetry.addData("cs2 data", cs2Data);
        telemetry.addData("spindexer", spindexer.getPosition());

        telemetry.addData("ball color", color);
        telemetry.update();

    }

    public boolean isFull(){
        return getCurrentLoad().size() == 3;
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


//    public void spinTargetToShooter(SortBall.BallColor target){
//        for (int i = 0; i < currentLoad.size(); i++) {
//            if (currentLoad.get(i) == target) {
//                spindexer.setPosition(OUTTAKE_SLOT_POS[i]);
//                break;
//            }
//        }
//    }
//
//    public void spinToShooter(int count) throws InterruptedException{
//        releaseBall();
//
//        for(int i = 1; i < count; i++) {
//            spindexer.setPosition(OUTTAKE_SLOT_POS[i]);
//            releaseBall();
//            sleep(200);
//        }
//    }

}
