package badgerutils.networktables;

import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;
import org.littletonrobotics.junction.networktables.LoggedNetworkBoolean;

import java.util.function.Consumer;

public final class LoggedNetworkTablesBuilder {

    private LoggedNetworkTablesBuilder(){}
    
    /**
     * Creates a {@link Trigger} instance that is bound to a boolean value at {@code key} on NetworkTables.
     *
     * @param key the key on NetworkTables
     * @param eventLoop the eventLoop to bind the Trigger to
     *
     * @return a Trigger with a toggle based on a boolean NetworkTables entry
     */
    public Trigger createLoggedButton(String key, EventLoop eventLoop){
        LoggedNetworkBoolean loggedNetworkBoolean = new LoggedNetworkBoolean(key, false);
        
        return new Trigger(eventLoop, loggedNetworkBoolean);
    }

    /**
     * {@code eventLoop} defaults to the default button loop of the {@link CommandScheduler}
     *
     * @see #createLoggedButton(String, EventLoop)
     */
    public Trigger createLoggedButton(String key){
        return createLoggedButton(key, CommandScheduler.getInstance().getDefaultButtonLoop());
    }

    /**
     * Creates a {@link Trigger} that resets its NetworkTables entry to false, after being true for 0.25 seconds.
     *
     * @see #createLoggedButton(String, EventLoop)
     */
    public Trigger createLoggedAutoResettingButton(String key, EventLoop eventLoop){
        LoggedNetworkBoolean loggedNetworkBoolean = new LoggedNetworkBoolean(key, false);

        return new Trigger(eventLoop, loggedNetworkBoolean).onTrue(Commands.waitSeconds(0.25)
                .andThen(new InstantCommand(() -> loggedNetworkBoolean.set(false)).ignoringDisable(true)));
    }

    /**
     * {@code eventLoop} defaults to the default button loop of the {@link CommandScheduler}
     *
     * @see #createLoggedAutoResettingButton(String, EventLoop)
     */
    public Trigger createLoggedAutoResettingButton(String key){
        return createLoggedAutoResettingButton(key, CommandScheduler.getInstance().getDefaultButtonLoop());
    }

    /**
     * Creates a {@link LoggedDashboardChooser} that contains the name of each Enum constant as an option.
     *
     * @param key the key on NetworkTables
     * @param tEnum the class of the Enum
     * @param startingValue the starting Enum value to use on the {@code LoggedDashboardChooser}
     * @param onValueChange a {@link Consumer} that gets called on startup, and whenever the selector changes with the
     * value it changed to
     * @param <T> the type of the Enum
     *
     * @return the created and published {@code LoggedDashboardChooser}
     */
    public static <T extends Enum<T>> LoggedDashboardChooser<Enum<T>> createSelectorFromEnum(String key, Class<T> tEnum, Enum<T> startingValue, Consumer<Enum<T>> onValueChange) {
        LoggedDashboardChooser<Enum<T>> chooser = new LoggedDashboardChooser<>(key);
        
        chooser.addDefaultOption(startingValue.toString(), startingValue);
        for (Enum<T> value : tEnum.getEnumConstants()) {
            if (value == startingValue) {
                continue;
            }
            chooser.addOption(value.toString(), value);
        }
        chooser.onChange(onValueChange);
        onValueChange.accept(startingValue);
        return chooser;
    }

    /**
     * {@code defaultValue} defaults to the first defined Enum constant
     *
     * @see #createSelectorFromEnum(String, Class, Enum, Consumer)
     */
    public static <T extends Enum<T>> LoggedDashboardChooser<Enum<T>> createSelectorFromEnum(String key, Class<T> tEnum, Consumer<Enum<T>> onValueChange) {
        return createSelectorFromEnum(key, tEnum, tEnum.getEnumConstants()[0], onValueChange);
    }
}
