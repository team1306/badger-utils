package frc.robot.subsystems.testsubsystem;

import org.littletonrobotics.junction.Logger;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;

public class TestSubsystem extends Mechanism {
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
    return run(coroutine -> {
          setDutyCycle(dutyCycle);
          coroutine.park();
        })
        .whenCanceled(
            () -> {
              setDutyCycle(0);
            })
        .named("RunDutyCycle");
  }
}
