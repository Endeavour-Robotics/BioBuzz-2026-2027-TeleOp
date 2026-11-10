package org.firstinspires.ftc.teamcode.rw.sys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.*;

public class robot {
    private IMU odo;
    Map<String, MotorEquation> kinematics = new HashMap<>();

    public biobuzz_config config = new biobuzz_config();
    public DcMotor fl, fr, bl, br;
    public DcMotor in, out;

    public TeleOp teleop = new TeleOp();
    public Auto auto = new Auto();

    public void init(biobuzz_config initializedConfig) {
        this.config = initializedConfig;

        this.fl = config.fl();
        this.fr = config.fr();
        this.bl = config.bl();
        this.br = config.br();
        this.in = config.in();
        this.out = config.out();
    }

    @FunctionalInterface
    public interface MotorEquation { double calculate(double fb, double rt, double cw); }
    public class TeleOp {
        public void drive(double fb, double rt, double cw) {
            double flSpeed = Objects.requireNonNull(config.kinematics.get("fl")).calculate(fb, rt, cw);
            double frSpeed = Objects.requireNonNull(config.kinematics.get("fr")).calculate(fb, rt, cw);
            double blSpeed = Objects.requireNonNull(config.kinematics.get("bl")).calculate(fb, rt, cw);
            double brSpeed = Objects.requireNonNull(config.kinematics.get("br")).calculate(fb, rt, cw);

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
            double flSpeed = Objects.requireNonNull(config.kinematics.get("fl")).calculate(fb, rt, cw);
            double frSpeed = Objects.requireNonNull(config.kinematics.get("fr")).calculate(fb, rt, cw);
            double blSpeed = Objects.requireNonNull(config.kinematics.get("bl")).calculate(fb, rt, cw);
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
                if (out != null) out.setPower(speed);
            }
        }
    }

    public void stop(boolean inout) {
        if (fl != null) fl.setPower(0);
        if (fr != null) fr.setPower(0);
        if (bl != null) bl.setPower(0);
        if (br != null) br.setPower(0);

        if (inout){
            if (in != null) in.setPower(0);
            if (out != null) out.setPower(0);
        }
    }
}
