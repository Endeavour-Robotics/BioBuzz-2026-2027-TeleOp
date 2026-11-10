package org.firstinspires.ftc.teamcode.rw.sys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;

public class biobuzz_config {
    // RUNTIME VARIABLES
    private int robotTeam = 2; // 1: RW; 2: TT + Testing
    private Servo lift;
    private DcMotor br, bl, fr, fl, in, out;
    private IMU odo;
    public Map<String, robot.MotorEquation> kinematics = new HashMap<>();

    public void init(HardwareMap hwMap, boolean intakeMode, boolean outtakeMode, boolean movementMode, boolean odometryMode) {
        // Motors
        if (intakeMode) { in = hwMap.get(DcMotor.class, "Intake"); }
        if (outtakeMode) { out = hwMap.get(DcMotor.class, "Outtake"); lift = hwMap.get(Servo.class, "launch switch"); }
        if (odometryMode) { odo = hwMap.get(IMU.class, "imu"); }
        if (movementMode) {
            br = hwMap.get(DcMotor.class, "BackRight");
            bl = hwMap.get(DcMotor.class, "BackLeft");
            fr = hwMap.get(DcMotor.class, "FrontRight");
            fl = hwMap.get(DcMotor.class, "FrontLeft");
        }


        if (intakeMode) { in.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); }
        if (outtakeMode) { out.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);}
        if (movementMode) {
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            if (robotTeam == 2) {
                bl.setDirection(DcMotor.Direction.REVERSE);
                fl.setDirection(DcMotor.Direction.REVERSE);
            } else if (robotTeam == 1) {
                fl.setDirection(DcMotor.Direction.REVERSE);
                fr.setDirection(DcMotor.Direction.REVERSE);
                br.setDirection(DcMotor.Direction.REVERSE);
            }

            br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // Motor Equations
        kinematics.put("br", (fb, rt, cw) -> fb - rt + cw);
        kinematics.put("bl", (fb, rt, cw) -> fb + rt - cw);
        kinematics.put("fr", (fb, rt, cw) -> fb - rt - cw);
        kinematics.put("fl", (fb, rt, cw) -> fb + rt + cw);
    }

    public DcMotor fl() { return fl; }
    public DcMotor fr() { return fr; }
    public DcMotor bl() { return bl; }
    public DcMotor br() { return br; }
    public DcMotor in() { return in; }
    public DcMotor out() { return out; }
    public Servo lift() { return lift; }
    public IMU odo() { return odo; }
}
