package badgerutils.triggers;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public final class AllianceTriggers {

    private AllianceTriggers(){}
    /**
     * Returns whether the Driverstation is on the red alliance
     *
     * @return true if on the red alliance, false if on the blue alliance or the alliance is not present
     */
    public static boolean isRedAlliance() {
        var alliance = DriverStation.getAlliance();
        return alliance.isEmpty() || (alliance.get() == DriverStation.Alliance.Red);
    }

    /**
     * Returns whether the Driverstation is on the blue alliance
     *
     * @return true if on the blue alliance or if the alliance is not present, false if the alliance is red
     */
    public static boolean isBlueAlliance() {
        return !isRedAlliance();
    }

    /**
     * Returns a trigger, activating when the alliance is red
     * @param eventLoop the event loop to use
     * @return the trigger
     */
    public static Trigger redAlliance(EventLoop eventLoop){
        return new Trigger(eventLoop, AllianceTriggers::isRedAlliance);
    }

    /**
     * Returns a trigger, activating when the alliance is blue
     * @param eventLoop the event loop to use
     * @return the trigger
     */
    public static Trigger blueAlliance(EventLoop eventLoop){
        return new Trigger(eventLoop, AllianceTriggers::isBlueAlliance);
    }
}
