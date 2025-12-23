package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class colorcheck {

    private Telemetry telemetry;
    private volatile String currentlyBanningVar = null;

    public int statusA = 0;
    public int statusB = 0;
    public int statusC = 0;

    private static final long BAN_DURATION_MS = 2000;

    public void SoSanh(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public colorcheck(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    // Hàm processAllVariables chỉ gọi logic
    public void processAllVariables(int a, int b, int c, int raw) throws InterruptedException {
        if(statusA == 0) {
            performLogic(a, raw, "A");
        }if(statusA == 1 && statusB == 0) {
            performLogic(b, raw, "B");
        }if(statusA == 1 && statusB == 1 && statusC == 0) {
            performLogic(c, raw, "C");
            statusA = 0;
            statusB = 0;
            statusC = 0;
        }
    }
    private void updateStatus(String varName, int value) throws InterruptedException {
        switch (varName) {
            case "A":
                statusA = value;
                Thread.sleep(2000);
                break;
            case "B":
                statusB = value;
                Thread.sleep(2000);
                break;
            case "C":
                statusC = value;
                Thread.sleep(2000);
                break;
        }
    }

    private void performLogic(int varValue, int raw, String varName) throws InterruptedException {

        int minRaw, maxRaw;
        // Logic xác định minRaw, maxRaw
        if (varValue == 0) {
            minRaw = 400; maxRaw = 500;
        } else if (varValue == 1) {
            minRaw = 750; maxRaw = 850;
        } else {
            return;
        }

        // --- Kiểm tra điều kiện ---
        if (raw >= minRaw && raw <= maxRaw) {
            performBanAction(); // Bỏ thông tin chi tiết
            updateStatus(varName, 1); // Đặt trạng thái là 0 (Đã BAN/RESET)

        } else {
            // Điều kiện sai:
            if (currentlyBanningVar != null && currentlyBanningVar.equals(varName)) {
                currentlyBanningVar = null;
            }
            // Đảm bảo trạng thái là 0 nếu không thỏa mãn
            updateStatus(varName, 0);
        }
    }


    private void performBanAction() throws InterruptedException {
        telemetry.addData("ACTION", "BANN...");
        telemetry.update();
        Thread.sleep(BAN_DURATION_MS);
    }
}