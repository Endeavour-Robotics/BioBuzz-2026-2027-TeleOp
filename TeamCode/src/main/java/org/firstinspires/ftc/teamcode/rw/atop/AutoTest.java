package org.firstinspires.ftc.teamcode.rw.atop;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Console;
import org.firstinspires.ftc.teamcode.rw.sys.robot;

@Autonomous(name="AutoTest", group="tests")
public class AutoTest extends OpMode {
    private Console console;
    robot robot = new robot();
    Telemetry tm;
    String string_state = "fw";
    enum State {
        WAIT_FOR_A,
        WAIT_FOR_B,
        WAIT_FOR_X,
        FINISHED,
        FORWARDS
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
    public void init() {
        tm = telemetry;
        tm.setDisplayFormat(Telemetry.DisplayFormat.HTML);
        console = new Console(tm);

        robot.init(hardwareMap, intakeMode, outtakeMode, movementMode, odometryMode);
        state = State.FORWARDS;
    }

    @Override
    public void loop() {
        switch (state) {
            case FORWARDS: {
                robot.auto.drive(0.25);
                sleep(1000);
                robot.auto.drive(0);
                sleep(500);
                robot.auto.drive(1);
                break;
            }
            case WAIT_FOR_B: {
                console.log("State is", string_state);
                console.info("To exit '" + state + "', press B");
                if (gamepad1.b && gamepad1.bWasReleased()) { state = State.WAIT_FOR_X; string_state = "wfx"; }
                break;
            }
            case WAIT_FOR_X: {
                console.log("State is", string_state);
                console.info("To exit '" + state + "', press X");
                if (gamepad1.x && gamepad1.xWasReleased()) { state = State.FINISHED; string_state = "f"; }
                break;
            }
            default: {
                console.log("Auto State Machine finished.");
            }
        }
        if (debugMode) debug();
    }

    public void debug() {
        console.log("State is", string_state);
    }
}
