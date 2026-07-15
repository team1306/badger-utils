package frc.robot.subsystems.testsubsystem;

import badgerutils.advantagekit.PIDTunable;
import badgerutils.advantagekit.cancoder.CANCoderSignals;
import badgerutils.advantagekit.talonfx.TalonFXSignals;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

public class TestIOReal implements TestIO {
  private final TalonFX leftMotor;
  private final TalonFX rightMotor;
  private final CANcoder encoder;

  private final TalonFXSignals leftMotorSignals;
  private final TalonFXSignals rightMotorSignals;
  private final CANCoderSignals encoderSignals;

  private final PIDTunable pidTunable;

  public TestIOReal() {
    leftMotor = new TalonFX(0);
    rightMotor = new TalonFX(1);
    encoder = new CANcoder(2);

    leftMotor.getConfigurator().apply(TestConstants.CW_CONFIG);
    rightMotor.getConfigurator().apply(TestConstants.CW_CONFIG);

    leftMotorSignals = new TalonFXSignals(leftMotor);
    rightMotorSignals = new TalonFXSignals(rightMotor);
    encoderSignals = new CANCoderSignals(encoder);

    pidTunable =
        new PIDTunable(
            "Test", SlotConfigs.from(TestConstants.CW_CONFIG.Slot0), leftMotor, rightMotor);
  }

  @Override
  public void updateInputs(TestIOInputs inputs) {
    inputs.leftMotor = leftMotorSignals.createLoggedTalonFX();
    inputs.rightMotor = rightMotorSignals.createLoggedTalonFX();
    inputs.encoder = encoderSignals.createLoggedCANCoder();
  }
}
