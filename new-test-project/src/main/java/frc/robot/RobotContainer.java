package frc.robot;

import frc.robot.subsystems.testsubsystem.TestIO;
import frc.robot.subsystems.testsubsystem.TestIOReal;
import frc.robot.subsystems.testsubsystem.TestSubsystem;
import org.wpilib.command3.Command;

public class RobotContainer {
  private final TestSubsystem testSubsystem;

  public RobotContainer() {
    switch (Constants.currentMode) {
      case REAL:
        testSubsystem = new TestSubsystem(new TestIOReal());
        break;
      case SIM:
        testSubsystem = new TestSubsystem(new TestIOReal());
        break;
      default:
        testSubsystem = new TestSubsystem(new TestIO() {});
        break;
    }

    testSubsystem.setDefaultCommand(testSubsystem.runDutyCycleCommand(1));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
