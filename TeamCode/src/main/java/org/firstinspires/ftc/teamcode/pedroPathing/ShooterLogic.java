package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ShooterLogic {
    private ElapsedTime timer = new ElapsedTime();
    DcMotorEx MShooter;
    Servo SLift, SSort;
    double SHOOTER_MIN_VELOCITY = 1000;
    double SERVO_LIFT_UP_POS = 0.5;
    double SERVO_LIFT_DOWN_POS = 0.0;
    double LAUNCH_TIME = 500;
    double SORT_TIME = 200; // mock sort time for now, sort logic should return ready to launch flag
    boolean freeFire = true;

    private enum State {
        IDLE,
        SORT,
        LAUNCH,
        RESET
    }
    State state = State.IDLE;
    double remainingShot = 0;
    double shooterVelocity = 0;

    public void init(HardwareMap hmap) {
      MShooter = hmap.get(DcMotorEx.class, "shooter");
      SLift = hmap.get(Servo.class, "lift");
      SSort = hmap.get(Servo.class,"sort");
      // tune shooter code
    }

    public void update() {
        shooterVelocity = MShooter.getVelocity();

        switch (state) {
            case IDLE:
                if(remainingShot > 0) {
                    MShooter.setPower(1);
                    setState(State.SORT);
                }
                break;
            case SORT:
                // mock sort logic
                if(freeFire || timer.milliseconds() > SORT_TIME) {
                    setState(State.LAUNCH);
                }
                break;
            case LAUNCH:
                if(shooterVelocity >= SHOOTER_MIN_VELOCITY) {
                    remainingShot--;
                    SLift.setPosition(SERVO_LIFT_UP_POS);
                    setState(State.RESET);
                }
                break;
            case RESET:
                if(timer.milliseconds() < LAUNCH_TIME) break;

                SLift.setPosition(SERVO_LIFT_DOWN_POS);
                if(remainingShot > 0) {
                    setState(State.SORT);
                } else {
                    MShooter.setPower(0);
                    setState(State.IDLE);
                }
                break;
        }
    }

    public void fireShots(int numberOfShots, boolean freeFire) {
        if(state == State.IDLE) {
            remainingShot = numberOfShots;
            this.freeFire = freeFire;
        }
    }

    public boolean isBusy() {
        return state != State.IDLE;
    }


    private void setState(State newState) {
        timer.reset();
        state = newState;
    }

}
