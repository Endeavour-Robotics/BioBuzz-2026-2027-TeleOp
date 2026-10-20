package org.firstinspires.ftc.teamcode.rw.atop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Console;
import org.firstinspires.ftc.teamcode.rw.sys.robot;

@Autonomous(name="AutoTest", group="tests")
public class AutoTest extends LinearOpMode {
    private Console console;
    robot robot = new robot();
    Telemetry tm;
    String string_state = "fw";

    enum State { // 7 represents 'and' or '&'
        WAIT_FOR_A,
        WAIT_FOR_B,
        O7B_I7F7cR, // Out and backward to in and forward and crabwalk right
        F7R_B7cR, // Forward and right to backward and crabwalk right
        F_R_cR // Forward to right to crabwalk right
    }

    State state = State.WAIT_FOR_A;
    // TEMP VARIABLES
    int tmp;

    // RUNTIME VARIABLES
    boolean debugMode = true;
    boolean intakeMode = false;
    boolean outtakeMode = false;
    boolean movementMode = true;
    boolean odometryMode = false;

    @Override
    public void runOpMode() {
        tm = telemetry;
        tm.setDisplayFormat(Telemetry.DisplayFormat.HTML);
        console = new Console(tm);

        robot.init(hardwareMap, intakeMode, outtakeMode, movementMode, odometryMode);
        state = State.F_R_cR;

        waitForStart();

        while (opModeIsActive()) {
            switch (state) {
                case F_R_cR: {
                    // Namespace[Movement_Example - Step 1]
                    /* Move the robot forward, stop, and turn in place. */
                    console.info("Current State", "F_R_cR");
                    tm.update();
                    robot.auto.drive(0.25, 0, 0);
                    sleep(1000);
                    robot.auto.drive(0, 0, 0);
                    sleep(500);
                    robot.auto.drive(0, 0.5, 0);
                    sleep(1000);
                    robot.auto.drive(0, 0, 0);
                    sleep(500);
                    robot.auto.drive(0, 0, 0.25);
                    sleep(1000);
                    robot.auto.drive(0, 0, 0);
                    /* End; Takes 4 seconds */
                    state = State.F7R_B7cR;
                    break;
                }
                case F7R_B7cR: {
                    console.info("Current State", "F7R_B7cR");
                    tm.update();
                    robot.auto.drive(0.5, 0.3, 0);
                    sleep(1000);
                    robot.stop();
                    robot.auto.drive(-0.5, 0, 0.3);
                    break;
                }
                case O7B_I7F7cR: {
                    console.info("Current State", "F7R_B7cR");
                    tm.update();
                    robot.auto.spin("out", 0.5);
                    robot.auto.drive(-0.4, 0, 0);
                    sleep(1000);
                    robot.stop();
                    break;
                }
                default: {
                    console.log("Auto State Machine finished.");
                }
            }
            if (debugMode) debug();
        }
    }

    public void debug() {
        console.log("State is", string_state);
    }
}