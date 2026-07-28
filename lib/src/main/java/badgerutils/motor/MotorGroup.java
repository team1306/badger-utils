package badgerutils.motor;

import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class MotorGroup {
  private final TalonFX[] motors;
  private final Follower followRequest;

  private TalonFX leader;
  private ControlRequest lastRequest = new NeutralOut();

  public MotorGroup(TalonFX... motors) {
    if (motors.length == 0) {
      DriverStation.reportWarning("Warning: Motor Group Initialized With No Motors", true);
      this.motors = null;
      followRequest = null;
      return;
    }

    this.motors = motors;
    this.followRequest = new Follower(motors[0].getDeviceID(), MotorAlignmentValue.Aligned);

    Trigger leaderDisconnected = new Trigger(() -> !leader.isConnected());

    leaderDisconnected.onTrue(Commands.runOnce(() -> setControl(lastRequest)));
  }

  public void setControl(ControlRequest request) {
    if (motors.length == 0) {
      DriverStation.reportWarning("Warning: Cannot set control for 0 motors", false);
      return;
    }

    lastRequest = request;
    for (TalonFX motor : motors) {
      if (motor.isConnected()) {
        motor.setControl(request);

        followRequest.LeaderID = motor.getDeviceID();
        leader = motor;

        for (TalonFX talonFX : motors) {
          if (talonFX != motor) {
            talonFX.setControl(followRequest);
          }
        }

        return;
      }
    }
  }
}
