package badgerutils.advantagekit;

import org.littletonrobotics.junction.AutoLog;

public interface TestIO {
  @AutoLog
  public static class TestIOInputs {
    public LoggedTalonFX leftMotor;
    public LoggedTalonFX rightMotor;
    public LoggedCANCoder encoder;
  }

  public default void updateInputs(TestIOInputs inputs) {}

  public default void setDutyCycle(double dutyCycle) {}
}
