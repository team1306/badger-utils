package badgerutils.advantagekit.talonfx;

import static org.wpilib.units.Units.Amps;
import static org.wpilib.units.Units.Celsius;
import static org.wpilib.units.Units.Rotations;
import static org.wpilib.units.Units.RotationsPerSecond;
import static org.wpilib.units.Units.RotationsPerSecondPerSecond;
import static org.wpilib.units.Units.Volts;

import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.AngularAcceleration;
import org.wpilib.units.measure.AngularVelocity;
import org.wpilib.units.measure.Current;
import org.wpilib.units.measure.Temperature;
import org.wpilib.units.measure.Voltage;

import com.ctre.phoenix6.controls.ControlRequest;

/** A record that contains all of the fields that should be logged from a TalonFX motor */
public record LoggedTalonFX(
    /** the CAN ID of the motor */
    int id,
    /** whether the status code reads okay when signals are refreshed */
    boolean isMotorConnected,
    /** the velocity of the motor (Rotations per Second) */
    double velocity,
    /** the position of the motor (Rotations) */
    double position,
    /** the acceleration of the motor (Rotations per Second squared) */
    double acceleration,
    /** the temperature of the motor (Celsius) */
    double temp,
    /** the supply current of the motor (Amps) */
    double supplyCurrent,
    /** the stator current of the motor (Amps) */
    double statorCurrent,
    /** the applied voltage of the motor (Volts) */
    double voltage,
    /** the closed loop error of the motor (Whatever the PID unit is) */
    double closedLoopError,
    /** the closed loop target of the motor (Whatever the PID unit is) */
    double closedLoopTarget,
    /** the info about the current control request */
    String controlInfo) {

  public LoggedTalonFX(
      int id,
      boolean isMotorConnected,
      AngularVelocity velocity,
      Angle position,
      AngularAcceleration acceleration,
      Temperature temp,
      Current supplyCurrent,
      Current statorCurrent,
      Voltage voltage,
      double closedLoopError,
      double closedLoopTarget,
      ControlRequest controlRequest) {
    this(
        id,
        isMotorConnected,
        velocity.in(RotationsPerSecond),
        position.in(Rotations),
        acceleration.in(RotationsPerSecondPerSecond),
        temp.in(Celsius),
        supplyCurrent.in(Amps),
        statorCurrent.in(Amps),
        voltage.in(Volts),
        closedLoopError,
        closedLoopTarget,
        controlRequest.toString());
  }

  /**
   * Returns the velocity of the motor.
   *
   * @return the velocity of the motor.
   */
  public AngularVelocity getVelocity() {
    return RotationsPerSecond.of(velocity);
  }

  /**
   * Returns the position of the motor.
   *
   * @return the position of the motor.
   */
  public Angle getPosition() {
    return Rotations.of(position);
  }

  /**
   * Returns the acceleration of the motor.
   *
   * @return the acceleration of the motor.
   */
  public AngularAcceleration getAcceleration() {
    return RotationsPerSecondPerSecond.of(acceleration);
  }

  /**
   * Returns the temperature of the motor.
   *
   * @return the temperature of the motor.
   */
  public Temperature getTemp() {
    return Celsius.of(temp);
  }

  /**
   * Returns the supply current used by the motor.
   *
   * @return the supply current used by the motor.
   */
  public Current getSupplyCurrent() {
    return Amps.of(supplyCurrent);
  }

  /**
   * Returns the stator current used by the motor.
   *
   * @return the stator current used by the motor.
   */
  public Current getStatorCurrent() {
    return Amps.of(statorCurrent);
  }

  /**
   * Returns the applied voltage of the motor.
   *
   * @return the applied voltage of the motor.
   */
  public Voltage getVoltage() {
    return Volts.of(voltage);
  }
}
