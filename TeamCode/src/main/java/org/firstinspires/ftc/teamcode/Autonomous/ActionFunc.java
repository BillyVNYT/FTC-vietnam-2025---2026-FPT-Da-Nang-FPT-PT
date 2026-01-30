package org.firstinspires.ftc.teamcode.Autonomous;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.IntakeFPT2;
import org.firstinspires.ftc.teamcode.utils.ShooterFPT2;

public class ActionFunc {
    public IntakeFPT2 intake;
    public ShooterFPT2 shooter;
    ActionFunc(HardwareMap hardwareMap){
        intake = new IntakeFPT2(hardwareMap);
        shooter = new ShooterFPT2(hardwareMap, intake);
    }
    public class shootBall implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shooter.setMotorVelocity(1675);
            intake.HoldBall = false;
            intake.checkHoldBall();
            intake.isActive();
            return false;
        }
    }
    public Action ShootBall(){
        return new shootBall();
    }

    public class closeShootBall implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            shooter.setMotorVelocity(1300);
            intake.HoldBall = true;
            intake.checkHoldBall();
            intake.stop();
            return false;
        }
    }
    public Action CloseShootBall(){
        return new shootBall();
    }
}
