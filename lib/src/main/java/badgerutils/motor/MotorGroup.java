package badgerutils.motor;

import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import badgerutils.commands.Commands;

import org.wpilib.command3.Command;
import org.wpilib.command3.Trigger;

/**
 * Manages a group of {@link TalonFX} motor controllers with dynamic leader failover capabilities. *
 *
 * <p>This class coordinates control requests across multiple motors, designating one active leader
 * while configuring remaining motors as followers. It automatically handles leader reassignment if
 * any motor in the group changes its connection state.
 */
public class MotorGroup {
  private final TalonFX[] motors;
  private final Follower followRequest;

  private TalonFX leader;
  private ControlRequest lastRequest = new NeutralOut();

  /**
   * Constructs a new {@code MotorGroup} with a designated primary motor and optional additional
   * motors. *
   *
   * <p>Sets up connection triggers on every motor to invoke {@link #chooseLeader()} whenever a
   * motor's connection state toggles.
   *
   * @param motor the first {@link TalonFX} motor
   * @param motors additional {@link TalonFX} motors to include in the group
   */
  public MotorGroup(TalonFX motor, TalonFX... motors) {
    this.motors = new TalonFX[motors.length + 1];
    this.motors[0] = motor;
    System.arraycopy(motors, 0, this.motors, 1, motors.length);

    this.followRequest = new Follower(this.motors[0].getDeviceID(), MotorAlignmentValue.Aligned);
    this.leader = this.motors[0];

    Command leaderChooser =
        Commands.createInstantCommand(() -> chooseLeader()).named("MotorGroupWatcher");
    for (TalonFX talonFX : this.motors) {
      Trigger disconnected = new Trigger(() -> !talonFX.isConnected());
      disconnected.onTrue(leaderChooser);
      disconnected.onFalse(leaderChooser);
    }
  }

  /**
   * Scans through the motor array to assign the first connected motor as the new leader. *
   *
   * <p>Once a responsive leader is designated, {@link #setControl(ControlRequest)} is re-invoked
   * using the last active {@link ControlRequest} to update motor routing.
   */
  public void chooseLeader() {
    for (TalonFX motor : motors) {
      if (motor.isConnected()) {
        followRequest.LeaderID = motor.getDeviceID();
        leader = motor;

        break;
      }
    }

    setControl(lastRequest);
  }

  /**
   * Sends a control request to the current leader motor and instructs all follower motors to follow
   * the leader
   *
   * @param request the {@link ControlRequest} to apply to the leader motor
   */
  public void setControl(ControlRequest request) {
    lastRequest = request;

    for (TalonFX motor : motors) {
      if (motor == leader) {
        motor.setControl(request);
      } else {
        motor.setControl(followRequest);
      }
    }
  }
}
