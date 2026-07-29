package frc.robot.subsystems.testsubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class TestSubsystem extends SubsystemBase {
  private final TestIO io;
  private final TestIOInputsAutoLogged inputs = new TestIOInputsAutoLogged();

  public TestSubsystem(TestIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Test", inputs);
  }

  public void setDutyCycle(double dutyCycle) {
    io.setDutyCycle(dutyCycle);
  }

  public Command runDutyCycleCommand(double dutyCycle) {
    return Commands.startEnd(() -> setDutyCycle(dutyCycle), () -> setDutyCycle(0), this);
  }
}
