package frc.robot.subsystems.testsubsystem;

import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.hardware.TalonFX;

import badgerutils.advantagekit.PIDTunable;
import badgerutils.advantagekit.talonfx.TalonFXSignals;

public class TestIOReal implements TestIO {
  private final TalonFX leftMotor;
  private final TalonFX rightMotor;

  private final TalonFXSignals leftMotorSignals;
  private final TalonFXSignals rightMotorSignals;

  private final PIDTunable pidTunable;

  public TestIOReal() {
    this.leftMotor = new TalonFX(0);
    this.rightMotor = new TalonFX(1);

    leftMotor.getConfigurator().apply(TestConstants.CW_CONFIG);
    rightMotor.getConfigurator().apply(TestConstants.CW_CONFIG);

    this.leftMotorSignals = new TalonFXSignals(leftMotor);
    this.rightMotorSignals = new TalonFXSignals(rightMotor);

    this.pidTunable = new PIDTunable("Test", SlotConfigs.from(TestConstants.CW_CONFIG.Slot0), leftMotor, rightMotor);    
  }

  @Override
  public void updateInputs(TestIOInputs inputs) {
    inputs.leftMotor = leftMotorSignals.createLoggedTalonFX();
    inputs.rightMotor = rightMotorSignals.createLoggedTalonFX();
  }
}
