package badgerutils.advantagekit;

import com.ctre.phoenix6.hardware.CANCoder;
import com.ctre.phoenix6.hardware.TalonFX;

public class TestIOReal implements TestIO {
  private TalonFX leftMotor;
  private TalonFX rightMotor;
  private CANCoder encoder;

  private TalonFXSignals leftMotorSignals;
  private TalonFXSignals rightMotorSignals;
  private CANCoderSignals encoderSignals;

  public TestIOReal() {
    this.leftMotor = new TalonFX(0);
    this.rightMotor = new TalonFX(1);
    this.encoder = new CANCoder(2);

    this.encoderSignals = new CANCoderSignals(encoder);
    this.leftMotorSignals = new TalonFXSignals(leftMotor);
    this.rightMotorSignals = new TalonFXSignals(rightMotor);
  }

  @Override
  public void updateInputs(TestIOInputs inputs) {
    inputs.leftMotor = leftMotorSignals.createLoggedTalonFX();
    inputs.rightMotor = rightMotorSignals.createLoggedTalonFX();
    inputs.encoder = encoderSignals.createLoggedCANCoder();
  }
}
