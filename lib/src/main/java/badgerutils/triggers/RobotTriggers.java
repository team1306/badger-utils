package badgerutils.triggers;

import org.wpilib.command3.Scheduler;
import org.wpilib.command3.Trigger;
import org.wpilib.driverstation.RobotState;
import org.wpilib.event.EventLoop;

public final class RobotTriggers {
  private RobotTriggers() {}

  /**
   * Returns a trigger that is true when the robot is enabled in autonomous mode.
   *
   * @return A trigger that is true when the robot is enabled in autonomous mode.
   */
  public static Trigger autonomous(EventLoop eventLoop) {
    return new Trigger(Scheduler.getDefault(), eventLoop, RobotState::isAutonomousEnabled);
  }

  /**
   * Returns a trigger that is true when the robot is enabled in autonomous mode. Uses the default
   * event loop
   *
   * @return A trigger that is true when the robot is enabled in autonomous mode.
   */
  public static Trigger autonomous() {
    return autonomous(Scheduler.getDefault().getDefaultEventLoop());
  }

  /**
   * Returns a trigger that is true when the robot is enabled in teleop mode.
   *
   * @return A trigger that is true when the robot is enabled in teleop mode.
   */
  public static Trigger teleop(EventLoop eventLoop) {
    return new Trigger(Scheduler.getDefault(), eventLoop, RobotState::isTeleopEnabled);
  }

  /**
   * Returns a trigger that is true when the robot is enabled in teleop mode. Uses the default event
   * loop
   *
   * @return A trigger that is true when the robot is enabled in teleop mode.
   */
  public static Trigger teleop() {
    return teleop(Scheduler.getDefault().getDefaultEventLoop());
  }

  /**
   * Returns a trigger that is true when the robot is disabled.
   *
   * @return A trigger that is true when the robot is disabled.
   */
  public static Trigger disabled(EventLoop eventLoop) {
    return new Trigger(Scheduler.getDefault(), eventLoop, RobotState::isDisabled);
  }

  /**
   * Returns a trigger that is true when the robot is disabled. Uses the default event loop
   *
   * @return A trigger that is true when the robot is disabled.
   */
  public static Trigger disabled() {
    return disabled(Scheduler.getDefault().getDefaultEventLoop());
  }
}
