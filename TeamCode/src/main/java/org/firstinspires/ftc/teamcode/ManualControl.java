package org.firstinspires.ftc.teamcode;
import org.firstinspires.ftc.teamcode.SortBall;
import static org.firstinspires.ftc.teamcode.SortBall.IsFull;
import static org.firstinspires.ftc.teamcode.SortBall.BallColor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.SortBall;

public class ManualControl {

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

        boolean hasPurpleBall = false;
        for (SortBall.BallColor ball : load) {
            if (ball == SortBall.BallColor.PURPLE) {
                hasPurpleBall = true;
                break;
            }
        }

        if (Gamepad1.left_bumper && hasPurpleBall) {
            MShooter1.setPower(1);
            MShooter2.setPower(1);
        } else {
            MShooter1.setPower(0);
            MShooter2.setPower(0);
        }
    }
    public void ShootGreenArtifact() {

        boolean hasGreenBall = false;

        for (SortBall.BallColor ball : load) {
            if (ball == SortBall.BallColor.GREEN) {
                hasGreenBall = true;
                break;
            }
        }


        if (Gamepad1.right_bumper && hasGreenBall) {
            MShooter1.setPower(1);
            MShooter2.setPower(1);
        } else {
            MShooter1.setPower(0);
            MShooter2.setPower(0);
        }
    }
}



