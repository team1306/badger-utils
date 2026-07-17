package badgerutils.advantagekit.talonfx;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularAcceleration;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;

/**
 * A class that stores all of the desired signals from a TalonFX motor. This class is used to create
 * a LoggedTalonFX object, which can be serialized and logged.
 */
public class TalonFXSignals {
  private final TalonFX motor;

  private final StatusSignal<AngularVelocity> velocity;
  private final StatusSignal<Angle> position;
  private final StatusSignal<AngularAcceleration> acceleration;
  private final StatusSignal<Temperature> temperature;
  private final StatusSignal<Current> supplyCurrent;
  private final StatusSignal<Current> statorCurrent;
  private final StatusSignal<Voltage> voltage;

  /**
   * Creates a new LoggedTalonFXSignals object that will store all of the desired signals from the
   * given TalonFX motor.
   *
   * @param motor the TalonFX motor from which to read signals
   */
  public TalonFXSignals(TalonFX motor) {
    this.motor = motor;

    velocity = motor.getVelocity();
    position = motor.getPosition();
    acceleration = motor.getAcceleration();
    temperature = motor.getDeviceTemp();
    supplyCurrent = motor.getSupplyCurrent();
    statorCurrent = motor.getStatorCurrent();
    voltage = motor.getMotorVoltage();
  }

  /**
   * Refreshes all of the signals from the motor and checks if the motor is connected.
   *
   * @return true if the motor is connected, false otherwise
   */
  public boolean refreshAndCheckConnection() {
    StatusCode status =
        BaseStatusSignal.refreshAll(
            velocity, position, acceleration, temperature, supplyCurrent, statorCurrent, voltage);

    return status.isOK();
  }

  /**
   * Creates a new LoggedTalonFX object that contains all of the current values of the signals from
   * the motor.
   *
   * @return the created LoggedTalonFX object
   */
  public LoggedTalonFX createLoggedTalonFX() {
    boolean isConnected = refreshAndCheckConnection();

    return new LoggedTalonFX(
        motor.getDeviceID(),
        isConnected,
        velocity.getValue(),
        position.getValue(),
        acceleration.getValue(),
        temperature.getValue(),
        supplyCurrent.getValue(),
        statorCurrent.getValue(),
        voltage.getValue());
  }
}
