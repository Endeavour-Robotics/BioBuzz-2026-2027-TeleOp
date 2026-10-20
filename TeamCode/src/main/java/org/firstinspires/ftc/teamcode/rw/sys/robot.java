package org.firstinspires.ftc.teamcode.rw.sys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import java.util.*;

public class robot {
    private DcMotor br;
    private DcMotor bl;
    private DcMotor fr;
    private DcMotor fl;
    private DcMotor in;
    private DcMotor out;
    private IMU odo;
    Map<String, MotorEquation> kinematics = new HashMap<>();

    public TeleOp teleop = new TeleOp();
    public Auto auto = new Auto();

    public void init(HardwareMap hwMap, boolean intakeMode, boolean outtakeMode, boolean movementMode, boolean odometryMode) {
        // Touch (None)

        // Motors
        if (intakeMode) { in = hwMap.get(DcMotor.class, "Intake"); }
        if (outtakeMode) { out = hwMap.get(DcMotor.class, "Outtake");}
        if (odometryMode) { odo = hwMap.get(IMU.class, "imu"); }
        if (movementMode) {
            br = hwMap.get(DcMotor.class, "BackRight");
            bl = hwMap.get(DcMotor.class, "BackLeft");
            fr = hwMap.get(DcMotor.class, "FrontRight");
            fl = hwMap.get(DcMotor.class, "FrontLeft");
        }


        if (intakeMode) { in.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); }
        if (outtakeMode) { out.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); }
        if (movementMode) {
            br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            bl.setDirection(DcMotor.Direction.REVERSE);
            fl.setDirection(DcMotor.Direction.REVERSE);

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

        public void take(String inout, double takePower) {
            if (inout == "in") {
                in.setPower(takePower);
            } else if (inout == "out") {
                out.setPower(takePower);
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
            if (inout == "in" || inout == "intake") {
                if (in != null) in.setPower(speed);
            }

            if (inout == "out" || inout == "outtake") {
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
