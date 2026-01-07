package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.teamcode.SortBall;
import static org.firstinspires.ftc.teamcode.SortBall.IsFull;
import static org.firstinspires.ftc.teamcode.SortBall.BallColor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.SortBall;
import com.qualcomm.robotcore.hardware.Servo;

public class ManualControl {
    enum ShootState {
        IDLE,
        ROTATING,
        SHOOTING
    }

    ShootState shootState = ShootState.IDLE;
    long shootTimer = 0;

    Servo alignServo;
    double[] SLOT_POS = {0.0, 0.33, 0.66};

    private void rotateToBall(SortBall.BallColor target) {
        for (int i = 0; i < load.length; i++) {
            if (load[i] == target) {
                alignServo.setPosition(SLOT_POS[i]);
                break;
            }
        }
    }

    double[] SPIN_POS = {0.0, 0.33, 0.66};
    DcMotor MTurnOuttake;
    DcMotor MOuttakeShooter;
    DcMotor MShooter1;
    DcMotor MShooter2;
    DcMotor MIntakeShaft;
    Gamepad Gamepad1;
    Gamepad Gamepad2;
    SortBall.BallColor[] load = {
            SortBall.BallColor.GREEN,
            SortBall.BallColor.PURPLE,
            SortBall.BallColor.EMPTY
    };

    boolean takeBall = true;

    private boolean hasBall(SortBall.BallColor color) {
        for (SortBall.BallColor ball : load) {
            if (ball == color) return true;
        }
        return false;
    }

    public ManualControl(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        MTurnOuttake = hardwareMap.get(DcMotor.class, "MTurnOuttake");
        MIntakeShaft = hardwareMap.get(DcMotor.class, "MIntakeShaft");
        MOuttakeShooter = hardwareMap.get(DcMotor.class, "MOuttakeShooter");
        MShooter1 = hardwareMap.get(DcMotor.class, "MShooter1");
        MShooter2 = hardwareMap.get(DcMotor.class, "MShooter2");
    }

    public void ControlTurnOutTake() {
        if (Gamepad2.right_trigger >= 0.1) {
            MTurnOuttake.setPower(Gamepad2.right_trigger);
        } else if (Gamepad2.left_trigger >= 0.1) {
            MTurnOuttake.setPower(-Gamepad2.left_trigger);
        } else {
            MTurnOuttake.setPower(0);
        }
    }

    public void ControlIntakeShaft() {
        if (SortBall.IsFull(load)) {
            MIntakeShaft.setPower(1);
        }
        if (Gamepad1.triangle) {
            takeBall = !takeBall;
            if (takeBall) {
                MIntakeShaft.setPower(1);
            } else
                MIntakeShaft.setPower(0);
        }
    }

    public void ControlOuttakeShooter() {
        if (Gamepad1.cross) {
            while (SortBall.IsFull(load))
                MOuttakeShooter.setPower(1);
        } else
            MOuttakeShooter.setPower(0);
    }

    public void ShootPurpleArtifact() {

        if (shootState == ShootState.IDLE
                && Gamepad1.left_bumper
                && hasBall(SortBall.BallColor.PURPLE)) {

            rotateToBall(SortBall.BallColor.PURPLE);
            shootTimer = System.currentTimeMillis();
            shootState = ShootState.ROTATING;
        }

        if (shootState == ShootState.ROTATING) {
            if (System.currentTimeMillis() - shootTimer > 300) {
                MShooter1.setPower(1);
                MShooter2.setPower(1);
                shootTimer = System.currentTimeMillis();
                shootState = ShootState.SHOOTING;
            }
        }
    }
}
