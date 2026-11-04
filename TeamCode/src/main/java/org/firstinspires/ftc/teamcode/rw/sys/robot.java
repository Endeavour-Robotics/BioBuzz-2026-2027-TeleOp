package org.firstinspires.ftc.teamcode.rw.sys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.*;

public class robot {
    // RUNTIME VARIABLES
    private int robotTeam = 2; // 1: RW; 2: TT + Testing

    private Servo lift;
    private DcMotor br, bl, fr, fl, in, out;
    private IMU odo;
    Map<String, MotorEquation> kinematics = new HashMap<>();

    public TeleOp teleop = new TeleOp();
    public Auto auto = new Auto();

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

    @FunctionalInterface
    public interface MotorEquation { double calculate(double fb, double rt, double cw); }
    public class TeleOp {
        public void drive(double fb, double rt, double cw) {
            double flSpeed = Objects.requireNonNull(kinematics.get("fl")).calculate(fb, rt, cw);
            double frSpeed = Objects.requireNonNull(kinematics.get("fr")).calculate(fb, rt, cw);
            double blSpeed = Objects.requireNonNull(kinematics.get("bl")).calculate(fb, rt, cw);
            double brSpeed = Objects.requireNonNull(kinematics.get("br")).calculate(fb, rt, cw);

            // Prevent one wheel from being faster than the other //
            double maxi = Math.max(Math.abs(flSpeed), Math.abs(frSpeed));
            maxi = Math.max(maxi, Math.abs(blSpeed));
            maxi = Math.max(maxi, Math.abs(brSpeed));

            if (maxi > 1.0) {
                flSpeed /= maxi;
                frSpeed /= maxi;
                blSpeed /= maxi;
                brSpeed /= maxi;
            }
            //==========================//

            // Add limit //
            double max_speed = 0.7;
            flSpeed = Range.clip(flSpeed, -max_speed, max_speed);
            frSpeed = Range.clip(frSpeed, -max_speed, max_speed);
            blSpeed = Range.clip(blSpeed, -max_speed, max_speed);
            brSpeed = Range.clip(brSpeed, -max_speed, max_speed);

            if (fl != null) fl.setPower(flSpeed);
            if (fr != null) fr.setPower(frSpeed);
            if (bl != null) bl.setPower(blSpeed);
            if (br != null) br.setPower(brSpeed);
        }

        public void take(String inout, double takePower) {
            if (inout.equals("in")) {
                if (in != null) in.setPower(-takePower);
            } else if (inout.equals("out")) {
                if (out != null) out.setPower(takePower);
            }
        }
    }

    public class Auto {
        public void drive(double fb, double rt, double cw) {
            double flSpeed = Objects.requireNonNull(kinematics.get("fl")).calculate(fb, rt, cw);
            double frSpeed = Objects.requireNonNull(kinematics.get("fr")).calculate(fb, rt, cw);
            double blSpeed = Objects.requireNonNull(kinematics.get("bl")).calculate(fb, rt, cw);
            double brSpeed = Objects.requireNonNull(kinematics.get("br")).calculate(fb, rt, cw);

            // Prevent limit passing //
            double max = Math.max(Math.abs(flSpeed), Math.abs(frSpeed));
            max = Math.max(max, Math.abs(blSpeed));
            max = Math.max(max, Math.abs(brSpeed));

            if (max > 1.0) {
                flSpeed /= max;
                frSpeed /= max;
                blSpeed /= max;
                brSpeed /= max;
            }
            //==========================//

            if (fl != null) fl.setPower(flSpeed);
            if (fr != null) fr.setPower(frSpeed);
            if (bl != null) bl.setPower(blSpeed);
            if (br != null) br.setPower(brSpeed);
        }

        public void spin(String inout, double speed) {
            if (inout.equals("in") || inout.equals("intake")) {
                if (in != null) in.setPower(speed);
            }

            if (inout.equals("out") || inout.equals("outtake") || /* Some people don't know */ inout.equals("outake")) {
                if (in != null) in.setPower(speed);
            }
        }
    }

    public void stop() {
        if (fl != null) fl.setPower(0);
        if (fr != null) fr.setPower(0);
        if (bl != null) bl.setPower(0);
        if (br != null) br.setPower(0);
    }
}
