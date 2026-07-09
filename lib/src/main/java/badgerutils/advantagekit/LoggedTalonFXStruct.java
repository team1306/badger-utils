package badgerutils.advantagekit;

import edu.wpi.first.util.struct.Struct;
import java.nio.ByteBuffer;

/**
 * A struct that can serialize and deserialize a LoggedTalonFX object.
 */
public class LoggedTalonFXStruct implements Struct<LoggedTalonFX> {
  @Override
  public Class<LoggedTalonFX> getTypeClass() {
    return LoggedTalonFX.class;
  }

  @Override
  public String getTypeName() {
    return "LoggedTalonFX";
  }

  @Override
  public int getSize() {
    return kSizeInt32 + 1 + kSizeDouble * 7;
  }

  @Override
  public String getSchema() {
    return "int32 id;bool isMotorConnected;double velocity;double position;double acceleration;double temp;double supplyCurrent;double statorCurrent;double voltage";
  }

  @Override
  public LoggedTalonFX unpack(ByteBuffer bb) {
    int id = bb.getInt();
    boolean isMotorConnected = bb.get() != 0;
    double velocity = bb.getDouble();
    double position = bb.getDouble();
    double acceleration = bb.getDouble();
    double temp = bb.getDouble();
    double supplyCurrent = bb.getDouble();
    double statorCurrent = bb.getDouble();
    double voltage = bb.getDouble();

    return new LoggedTalonFX(
        id,
        isMotorConnected,
        velocity,
        position,
        acceleration,
        temp,
        supplyCurrent,
        statorCurrent,
        voltage);
  }

  @Override
  public void pack(ByteBuffer bb, LoggedTalonFX value) {
    bb.putInt(value.id());
    bb.put((byte) (value.isMotorConnected() ? 1 : 0));
    bb.putDouble(value.velocity());
    bb.putDouble(value.position());
    bb.putDouble(value.acceleration());
    bb.putDouble(value.temp());
    bb.putDouble(value.supplyCurrent());
    bb.putDouble(value.statorCurrent());
    bb.putDouble(value.voltage());
  }
}
