package badgerutils.advantagekit.cancoder;

import static org.wpilib.units.Units.Rotations;
import static org.wpilib.units.Units.RotationsPerSecond;

import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.AngularVelocity;

/** A record that contains all of the fields that should be logged from a CANcoder */
public record LoggedCANCoder(
    /** the CAN ID of the encoder */
    int id,
    /** whether the status code reads okay when signals are refreshed */
    boolean isConnected,
    /** the velocity of the encoder (Rotations per Second) */
    double velocity,
    /** the position of the encoder (Rotations) */
    double position,
    /** the absolute position of the encoder (Rotations) */
    double absolutePosition) {

  public LoggedCANCoder(
      int id,
      boolean isConnected,
      AngularVelocity velocity,
      Angle position,
      Angle absolutePosition) {
    this(
        id,
        isConnected,
        velocity.in(RotationsPerSecond),
        position.in(Rotations),
        absolutePosition.in(Rotations));
  }

  /**
   * Returns the velocity of the encoder.
   *
   * @return the velocity of the encoder.
   */
  public AngularVelocity getVelocity() {
    return RotationsPerSecond.of(velocity);
  }

  /**
   * Returns the position of the encoder.
   *
   * @return the position of the encoder.
   */
  public Angle getPosition() {
    return Rotations.of(position);
  }

  /**
   * Returns the absolute position of the encoder.
   *
   * @return the absolute position of the encoder.
   */
  public Angle getAbsolutePosition() {
    return Rotations.of(absolutePosition);
  }
}
