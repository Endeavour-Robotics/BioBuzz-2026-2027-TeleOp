package org.firstinspires.ftc.teamcode.rw.mop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Console;
import org.firstinspires.ftc.teamcode.rw.sys.robot;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="DebugLayerTest", group="tests")
public class DebugLayerTest extends OpMode {
    private Console console;
    robot robot = new robot();
    Telemetry tm;

    // RUNTIME VARIABLES
    boolean debugMode = true;

    // PUBLICITY VARIABLES
    double fb;
    double st;
    double cw;
    boolean intake = false;
    boolean outtake = false;
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

        robot.init(hardwareMap, true, true, true, true);
    }

    @Override
    public void loop() {
        fb = -gamepad1.right_stick_y; //forwards backwards; gamepad returns backwards
        st = gamepad1.right_stick_x; //strafe
        cw = gamepad1.left_stick_x; //crabwalk

        if (gamepad2.left_bumper && gamepad2.leftBumperWasReleased()) {
            intake = !intake;
        }

        if (gamepad2.right_bumper && gamepad2.rightBumperWasReleased()) {
            outtake = !outtake;
        }

        if (gamepad2.dpad_up && gamepad2.dpadUpWasReleased() && takerange < takepowers.size() - 1) {
            takerange = takerange + 1;
        }

        if (gamepad2.dpad_down && takerange > -1) {
            takerange = takerange - 1;
        }

        if (intake) {
            robot.teleop.take("in", 0.7);
        } else {
            robot.teleop.take("in", 0);
        }

        if (outtake) {
            robot.teleop.take("out", takepowers.get(takerange));
        } else {
            robot.teleop.take("out", 0);
        }

        if (Math.abs(fb) < 0.05) fb = 0;
        if (Math.abs(st) < 0.05) st = 0;
        if (Math.abs(cw) < 0.05) cw = 0;

        if (debugMode && gamepad2.dpad_right) debug();

        robot.teleop.drive(fb, st, cw);
    }

    public void debug() {
        console.h1("Debug Menu", "#DDD");
        console.info("In?" + intake);
        console.info("Out?" + outtake);
        console.log("Movement Values", "FB: " + fb + " | ST: " + st); // Forwards|Backwards Strafe
        console.log("CW:" + cw); // crabwalk
        tm.update();
    }
}
