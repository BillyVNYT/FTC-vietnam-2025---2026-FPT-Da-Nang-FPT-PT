package org.firstinspires.ftc.teamcode;
import com.bylazar.telemetry.TelemetryManager;

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
    SortBall(List<BallColor> obeliskData) {
        this.obeliskData = obeliskData;
    }

    public static boolean isFull(BallColor[] load){
        for(BallColor ball : load){
            if(ball == BallColor.EMPTY)
                return false;
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
}
