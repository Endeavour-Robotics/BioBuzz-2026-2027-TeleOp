package org.firstinspires.ftc.teamcode.rw.mop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Console;
import org.firstinspires.ftc.teamcode.rw.sys.biobuzz_config;
import org.firstinspires.ftc.teamcode.rw.sys.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@TeleOp(name="ManualOP", group="BioBuzz")
public class ManualOp_BioBuzz extends OpMode {
    private Console console;
    robot robot = new robot();
    Telemetry tm;
    biobuzz_config config;

    // RUNTIME VARIABLES
    boolean debugMode = true;
    boolean intakeMode = false;
    boolean outtakeMode = false;
    boolean movementMode = true;
    boolean odometryMode = false;
    boolean invert_controls = false;
    double deadZone = 0.1;
    double fb_fix = -1;

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

        robot.init(config);
        config.init(hardwareMap, intakeMode, outtakeMode, movementMode, odometryMode);
    }

    @Override
    public void loop() {
        if (!invert_controls) {
            fb = (fb_fix * gamepad1.left_stick_y); //forwards backwards; gamepad returns backwards
            rt = gamepad1.right_stick_x; //rotate
            cw = gamepad1.left_stick_x; //crabwalk
        } else {
            fb = (fb_fix * gamepad1.right_stick_y); //forwards backwards; gamepad returns backwards
            rt = gamepad1.left_stick_x; //rotate
            cw = gamepad1.right_stick_x; //crabwalk
        }

        if (Math.abs(fb) < deadZone) fb = 0;
        if (Math.abs(rt) < deadZone) rt = 0;
        if (Math.abs(cw) < deadZone) cw = 0;

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
                takerange++;
                if (takerange >= takepowers.size()) {
                    takerange = 0;
                }
            }

            if (gamepad2.dpad_down && gamepad2.dpadDownWasReleased() && takerange >= 0) {
                takerange--;
                if (takerange < 0) {
                    takerange = takepowers.size() - 1;
                }
            }
        }

        if ((gamepad2.dpad_right || gamepad1.dpad_right) && debugMode) debug = true;
        if (debug) debug();

        robot.teleop.drive(fb, rt, cw);
    }

    public void debug() {
        console.h1("Debug Menu", "#DDD");
        console.info("In?" + intake);
        console.info("Out?" + outtake);
        console.info("Takerange:" + takerange + "Take power:" + takepowers.get(takerange));
        console.log(String.format(Locale.ENGLISH, "Movement Values FB: %.2f | RT: %.2f | CW: %.2f", fb, rt, cw)); // Forwards|Backwards Rotate Crabwalk
        tm.update();
    }
}
//the Wi-Fi is bad here
//solution: use hotspot