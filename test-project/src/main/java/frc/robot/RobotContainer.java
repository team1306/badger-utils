package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.testsubsystem.TestIO;
import frc.robot.subsystems.testsubsystem.TestIOReal;
import frc.robot.subsystems.testsubsystem.TestSubsystem;

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
    return Commands.none();
  }
}
