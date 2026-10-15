package org.firstinspires.ftc.teamcode.rw.sys;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class testhelper {
    public void driveToPosition(int targetTicks, double power, double timeoutSeconds) {
        ElapsedTime timeoutTimer = new ElapsedTime();

        odo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        odo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        timeoutTimer.reset();

        while ( Math.abs(odo.getCurrentPosition()) < targetTicks &&
                timeoutTimer.seconds() < timeoutSeconds) {

            br.setPower(power);
            bl.setPower(power);
            fr.setPower(power);
            fl.setPower(power);

            telemetry.addData("Odo Position", odo.getCurrentPosition());
            telemetry.addData("Time Left", "%.1f", timeoutSeconds - timeoutTimer.seconds());
            telemetry.update();
        }

        br.setPower(0);
        bl.setPower(0);
        fr.setPower(0);
        fl.setPower(0);
    }
}
