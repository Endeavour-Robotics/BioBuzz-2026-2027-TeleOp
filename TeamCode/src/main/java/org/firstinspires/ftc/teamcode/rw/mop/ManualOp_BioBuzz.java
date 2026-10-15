package org.firstinspires.ftc.teamcode.rw.mop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Console;
import org.firstinspires.ftc.teamcode.rw.sys.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@TeleOp(name="ManualOP", group="BioBuzz")
public class ManualOp_BioBuzz extends OpMode {
    private Console console;
    robot robot = new robot();
    Telemetry tm;

    // RUNTIME VARIABLES
    boolean debugMode = true;
    boolean intakeMode = false;
    boolean outtakeMode = false;
    boolean movementMode = true;
    boolean odometryMode = false;
    double deadZone = 0.05;

    // PUBLICITY VARIABLES
    double fb, rt, cw;
    boolean intake = false, outtake = false;
    boolean debug;
    List<Double> takepowers = new ArrayList<>();
    int takerange;

    @Override
    public void init() {
        tm = telemetry;
        tm.setDisplayFormat(Telemetry.DisplayFormat.HTML);
        console = new Console(tm);

        takepowers.add(0.45);
        takepowers.add(0.65);
        takepowers.add(0.85);

        robot.init(hardwareMap, intakeMode, outtakeMode, movementMode, odometryMode);
    }

    @Override
    public void loop() {
        fb = -gamepad1.left_stick_y; //forwards backwards; gamepad returns backwards
        rt = gamepad1.left_stick_x; //rotate
        cw = gamepad1.right_stick_x; //crabwalk

        if (intakeMode) {
            if (gamepad2.left_bumper && gamepad2.leftBumperWasReleased()) {
                intake = !intake;
            }

            if (intake) {
                robot.teleop.take("in", 0.7);
            } else {
                robot.teleop.take("in", 0);
            }
        }

        if (outtakeMode) {
            if (gamepad2.right_bumper && gamepad2.rightBumperWasReleased()) {
                outtake = !outtake;
            }

            if (outtake) {
                robot.teleop.take("out", takepowers.get(takerange));
            } else {
                robot.teleop.take("out", 0);
            }

            if (gamepad2.dpad_up && gamepad2.dpadUpWasReleased() && takerange < takepowers.size() - 1) {
                if (takerange == 3) takerange = 0;
                else ++takerange;
            }

            if (gamepad2.dpad_down && gamepad2.dpadDownWasReleased() && takerange >= 0) {
                if (takerange == 0) takerange = 3;
                else --takerange;
            }
        }

        if (Math.abs(fb) < deadZone) fb = 0;
        if (Math.abs(rt) < deadZone) rt = 0;
        if (Math.abs(cw) < deadZone) cw = 0;

        if ((gamepad2.dpad_right || gamepad1.dpad_right) && debugMode) debug = true;
        if (debug) debug();

        robot.teleop.drive(fb, rt, cw);
    }

    public void debug() {
        console.h1("Debug Menu", "#DDD");
        console.info("In?" + intake);
        console.info("Out?" + outtake);
        console.info("Takerange:" + takerange + "Takepower:" + takepowers.get(takerange));
        console.log(String.format(Locale.ENGLISH, "Movement Values FB: %.2f | ST: %.2f | CW: %.2f", fb, rt, cw)); // Forwards|Backwards Strafe
        tm.update();
    }
}
//the wifi is bad here