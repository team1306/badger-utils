package badgerutils.triggers;

import org.wpilib.command3.Scheduler;
import org.wpilib.command3.Trigger;
import org.wpilib.driverstation.Alliance;
import org.wpilib.driverstation.MatchState;
import org.wpilib.event.EventLoop;

public final class AllianceTriggers {

  private AllianceTriggers() {}

  /**
   * Returns whether the Driverstation is on the red alliance
   *
   * @return true if on the red alliance, false if on the blue alliance or the alliance is not
   *     present
   */
  public static boolean isRedAlliance() {
    var alliance = MatchState.getAlliance();
    return alliance.isEmpty() || (alliance.get() == Alliance.RED);
  }

  /**
   * Returns whether the Driverstation is on the blue alliance
   *
   * @return true if on the blue alliance or if the alliance is not present, false if the alliance
   *     is red
   */
  public static boolean isBlueAlliance() {
    return !isRedAlliance();
  }

  /**
   * Returns a trigger, activating when the alliance is red
   *
   * @param eventLoop the event loop to use
   * @return the trigger
   */
  public static Trigger redAlliance(EventLoop eventLoop) {
    return new Trigger(Scheduler.getDefault(), eventLoop, AllianceTriggers::isRedAlliance);
  }

  /**
   * Returns a trigger, activating when the alliance is red. Uses the default event loop
   *
   * @return the trigger
   */
  public static Trigger redAlliance() {
    return redAlliance(Scheduler.getDefault().getDefaultEventLoop());
  }

  /**
   * Returns a trigger, activating when the alliance is blue
   *
   * @param eventLoop the event loop to use
   * @return the trigger
   */
  public static Trigger blueAlliance(EventLoop eventLoop) {
    return new Trigger(Scheduler.getDefault(), eventLoop, AllianceTriggers::isBlueAlliance);
  }

  /**
   * Returns a trigger, activating when the alliance is blue. Uses the default event loop
   *
   * @return the trigger
   */
  public static Trigger blueAlliance() {
    return blueAlliance(Scheduler.getDefault().getDefaultEventLoop());
  }
}
