package org.firstinspires.ftc.teamcode.rw.sys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArcadeRW {
    private DcMotor bl;
    private DcMotor fl;

    private DcMotor br;

    private DcMotor fr;

    public void init(HardwareMap hwMap) {
        bl = hwMap.get(DcMotor.class, "BackLeft");
        fl = hwMap.get(DcMotor.class, "FrontLeft");
        br = hwMap.get(DcMotor.class, "BackRight");
        fr = hwMap.get(DcMotor.class, "FrontRight");
        
        bl.setDirection(DcMotor.Direction.REVERSE);
        fl.setDirection(DcMotor.Direction.REVERSE);

        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }
    public void Drive(double throttle, double spin){
        double leftpwr = throttle + spin;
        double rightpwr = throttle - spin;

        double largest = Math.max(Math.abs(leftpwr), Math.abs(rightpwr));
        if(largest > 1.0){
            leftpwr /= largest;
            rightpwr /= largest;


        }
        bl.setPower(leftpwr);
        fl.setPower(leftpwr);
        br.setPower(rightpwr);
        fr.setPower(rightpwr);




    }



}