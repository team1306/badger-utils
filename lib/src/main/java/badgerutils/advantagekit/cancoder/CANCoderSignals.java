package badgerutils.advantagekit.cancoder;

import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.AngularVelocity;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;

/**
 * A class that stores all of the desired signals from a CANcoder. This class is used to create a
 * LoggedCANCoder object, which can be serialized and logged.
 */
public class CANCoderSignals {
  private final CANcoder encoder;

  private final StatusSignal<AngularVelocity> velocity;
  private final StatusSignal<Angle> position;
  private final StatusSignal<Angle> absolutePosition;

  /**
   * Creates a new LoggedCANCoderSignals object that will store all of the desired signals from the
   * given CANcoder.
   *
   * @param encoder the CANcoder from which to read signals
   */
  public CANCoderSignals(CANcoder encoder) {
    this.encoder = encoder;

    velocity = encoder.getVelocity();
    position = encoder.getPosition();
    absolutePosition = encoder.getAbsolutePosition();
  }

  /**
   * Refreshes all of the signals from the encoder and checks if the encoder is connected.
   *
   * @return true if the encoder is connected, false otherwise
   */
  public boolean refreshAndCheckConnection() {
    StatusCode status = BaseStatusSignal.refreshAll(velocity, position, absolutePosition);

    return status.isOK();
  }

  /**
   * Creates a new LoggedCANCoder object that contains all of the current values of the signals from
   * the encoder.
   *
   * @return the created LoggedCANCoder object
   */
  public LoggedCANCoder createLoggedCANCoder() {
    boolean isConnected = refreshAndCheckConnection();

    return new LoggedCANCoder(
        encoder.getDeviceID(),
        isConnected,
        velocity.getValue(),
        position.getValue(),
        absolutePosition.getValue());
  }
}
