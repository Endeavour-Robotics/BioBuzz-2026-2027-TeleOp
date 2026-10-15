package org.firstinspires.ftc.teamcode.rw.mop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.rw.sys.ArcadeRW;

@TeleOp(name="ArcadeDrive", group="tests")
public class ArcadeDrive extends OpMode {
    ArcadeRW drive = new ArcadeRW();
    double throttle, spin;

    @Override
    public void init() {drive.init(hardwareMap);}

    @Override
    public void loop () {
        throttle = -gamepad1.left_stick_y;
        spin = gamepad1.left_stick_x;
        drive.Drive(throttle, spin);
    }
}
//PS:The WIFI SUCKS here