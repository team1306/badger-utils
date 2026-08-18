package badgerutils.swerve;

import java.util.function.Supplier;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.geometry.Translation2d;
import org.wpilib.math.kinematics.ChassisVelocities;

/**
 * {@code AntiTipping} provides a proportional correction system to prevent the robot from tipping
 * over during operation.
 *
 * <p>It uses pitch and roll measurements (in degrees) to detect excessive inclination and computes
 * a correction velocity in the opposite direction of the tilt. The resulting correction can be
 * added to the robot’s translational velocity to help stabilize it.
 *
 * <h2>Usage</h2>
 *
 * <ol>
 *   <li>Instantiate with pitch and roll suppliers and initial configuration parameters.
 *   <li>Call {@link #calculate()} periodically (e.g. once per control loop).
 *   <li>Add the correction from {@link #getSpeeds()} to your drive command.
 * </ol>
 *
 * <h2>Configuration</h2>
 *
 * <ul>
 *   <li>{@link #setTippingThresholdDegrees(double)} — sets the tipping detection threshold in
 *       degrees.
 *   <li>{@link #setMaxCorrectionSpeed(double)} — sets the maximum correction velocity (m/s).
 * </ul>
 *
 * <p>The correction is purely proportional: {@code correction = kP * inclinationMagnitude}, and
 * clamped to {@code maxCorrectionSpeed}.
 *
 * @since 2025
 * @see <a
 *     href="https://www.chiefdelphi.com/t/introducing-antitipping-lib/508284">https://www.chiefdelphi.com/t/introducing-antitipping-lib/508284</a>
 */
public class AntiTipping {
  private final Supplier<Double> pitchSupplier;
  private final Supplier<Double> rollSupplier;
  private final double kP; // proportional gain
  private double tippingThresholdDegrees;
  private double maxCorrectionSpeed; // m/s
  private double pitch = 0.0;
  private double roll = 0.0;

  private double correctionSpeed = 0.0;

  private double inclinationMagnitude = 0.0;

  private double yawDirectionDeg = 0.0;

  private boolean isTipping = false;

  private Rotation2d tiltDirection = new Rotation2d();

  private ChassisVelocities speeds = new ChassisVelocities();

  /**
   * Creates a new {@code AntiTipping} instance.
   *
   * @param pitchSupplier supplier providing the current pitch angle (degrees)
   * @param rollSupplier supplier providing the current roll angle (degrees)
   * @param kP proportional gain for correction
   * @param tippingThresholdDegrees tipping detection threshold (degrees)
   * @param maxCorrectionSpeed maximum correction velocity (m/s)
   */
  public AntiTipping(
      Supplier<Double> pitchSupplier,
      Supplier<Double> rollSupplier,
      double kP,
      double tippingThresholdDegrees,
      double maxCorrectionSpeed) {

    this.pitchSupplier = pitchSupplier;
    this.rollSupplier = rollSupplier;
    this.kP = kP;
    this.tippingThresholdDegrees = tippingThresholdDegrees;
    this.maxCorrectionSpeed = maxCorrectionSpeed;
  }

  /**
   * Updates tipping detection and computes the proportional correction.
   *
   * <p>This method updates internal values (pitch, roll, direction, magnitude, etc.) and generates
   * a correction {@link ChassisSpeeds} vector that can be applied to stabilize the robot. It should
   * be called periodically (e.g. once per control loop).
   */
  public void calculate() {
    pitch = pitchSupplier.get();
    roll = rollSupplier.get();

    isTipping =
        Math.abs(pitch) > tippingThresholdDegrees || Math.abs(roll) > tippingThresholdDegrees;

    // Tilt direction (the direction the robot is falling towards)
    tiltDirection = new Rotation2d(Math.atan2(-roll, -pitch));
    yawDirectionDeg = tiltDirection.getDegrees();

    // Tilt magnitude (hypotenuse of pitch and roll)
    inclinationMagnitude = Math.hypot(pitch, roll);

    // Proportional correction
    correctionSpeed = kP * -inclinationMagnitude;
    correctionSpeed = Math.clamp(correctionSpeed, -maxCorrectionSpeed, maxCorrectionSpeed);

    // Correction vector (field-relative)
    Translation2d correctionVector =
        new Translation2d(0, 1).rotateBy(tiltDirection).times(correctionSpeed);

    // WPILib convention: Y axis inverted
    speeds = new ChassisVelocities(correctionVector.getX(), -correctionVector.getY(), 0);
  }

  /** Sets the tipping detection threshold in degrees. */
  public void setTippingThresholdDegrees(double tippingThresholdDegrees) {
    this.tippingThresholdDegrees = tippingThresholdDegrees;
  }

  /** Sets the maximum correction velocity in meters per second. */
  public void setMaxCorrectionSpeed(double maxCorrectionSpeed) {
    this.maxCorrectionSpeed = maxCorrectionSpeed;
  }

  /**
   * @return the most recent pitch value in degrees.
   */
  public double getPitch() {
    return pitch;
  }

  /**
   * @return the most recent roll value in degrees.
   */
  public double getRoll() {
    return roll;
  }

  public double getInclinationMagnitude() {
    return inclinationMagnitude;
  }

  public double getYawDirectionDeg() {
    return yawDirectionDeg;
  }

  /**
   * @return whether the robot is currently beyond the tipping threshold.
   */
  public boolean getIsTipping() {
    return isTipping;
  }

  public Rotation2d getTiltDirection() {
    return tiltDirection;
  }

  public ChassisVelocities getVelocities() {
    return speeds;
  }
}
