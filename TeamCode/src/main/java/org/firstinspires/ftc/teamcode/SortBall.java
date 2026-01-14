package org.firstinspires.ftc.teamcode;
import static java.lang.Thread.sleep;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utils.ColorSensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortBall {
    public enum BallColor {
        GREEN,
        PURPLE,
        EMPTY
    }
    List<BallColor> obeliskData;
    ColorSensor colorSensor1, colorSensor2, colorSensor3;
    Servo spindexer;

    SortBall(List<BallColor> obeliskData, HardwareMap hardwareMap) {
        this.obeliskData = obeliskData;
        colorSensor1 = hardwareMap.get(ColorSensor.class, "cs");
        colorSensor2 = hardwareMap.get(ColorSensor.class, "cs2");
        colorSensor3 = hardwareMap.get(ColorSensor.class, "cs3");
    }

    double[] INTAKE_SLOT_POS = {0.385, 0.455, 0.52};
    double[] OUTTAKE_SLOT_POS = {0.4, 0.5, 0.6};

    private List<SortBall.BallColor> currentLoad;

    public List<SortBall.BallColor> getCurrentLoad() {
        return currentLoad;
    }

    private void releaseBall(int idx) {
        currentLoad.remove(idx);
    }

    private void readyToShoot() {
        spindexer.setPosition(OUTTAKE_SLOT_POS[0]);
    }

    public void loadBallsIn() throws InterruptedException {
        BallColor color1 = colorSensor1.detectBallColor(2000);
        BallColor color2 = colorSensor2.detectBallColor(3000);
        BallColor color3 = colorSensor3.detectBallColor(4000);

        int size = currentLoad.size();
        boolean cs1Detected = !color1.equals(BallColor.EMPTY);
        boolean cs2Detected = !color2.equals(BallColor.EMPTY);
        boolean cs3Detected = !color3.equals(BallColor.EMPTY);
        BallColor color = cs1Detected ? color1 : (cs2Detected ? color2 : color3);

        if((cs1Detected || cs2Detected || cs3Detected) && size < 3) {
            currentLoad.add(color);
            spindexer.setPosition(INTAKE_SLOT_POS[size+1]);
            sleep(500);
        }

        if(size == 3) readyToShoot();

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


    public void rotateToBall(SortBall.BallColor target) {
        for (int i = 0; i <currentLoad.size(); i++) {
            if (currentLoad.get(i) == target) {
                spindexer.setPosition(INTAKE_SLOT_POS[i]);
                break;
            }
        }
    }

    public void rotateToShooter(int count) throws InterruptedException{
        releaseBall(0);

        for(int i = 1; i < count; i++) {
            spindexer.setPosition(OUTTAKE_SLOT_POS[i]);
            releaseBall(i);
            sleep(200);
        }
    }

}
