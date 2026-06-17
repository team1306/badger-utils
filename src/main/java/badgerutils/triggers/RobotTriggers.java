package badgerutils.triggers;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public final class RobotTriggers {
    private RobotTriggers() {}

    /**
     * Returns a trigger that is true when the robot is enabled in autonomous mode.
     *
     * @return A trigger that is true when the robot is enabled in autonomous mode.
     */
    public static Trigger autonomous(EventLoop eventLoop) {
        return new Trigger(eventLoop, DriverStation::isAutonomousEnabled);
    }

    /**
     * Returns a trigger that is true when the robot is enabled in teleop mode.
     *
     * @return A trigger that is true when the robot is enabled in teleop mode.
     */
    public static Trigger teleop(EventLoop eventLoop) {
        return new Trigger(eventLoop, DriverStation::isTeleopEnabled);
    }

    /**
     * Returns a trigger that is true when the robot is disabled.
     *
     * @return A trigger that is true when the robot is disabled.
     */
    public static Trigger disabled(EventLoop eventLoop) {
        return new Trigger(eventLoop, DriverStation::isDisabled);
    }

    /**
     * Returns a trigger that is true when the robot is enabled in test mode.
     *
     * @return A trigger that is true when the robot is enabled in test mode.
     */
    public static Trigger test(EventLoop eventLoop) {
        return new Trigger(eventLoop, DriverStation::isTestEnabled);
    }
}
