package badgerutils.networktables;

import static org.wpilib.units.Units.Seconds;

import java.util.function.Consumer;
import org.littletonrobotics.junction.networktables.LoggedNetworkBoolean;
import org.littletonrobotics.junction.networktables.LoggedNetworkChooser;
import org.wpilib.command3.Command;
import org.wpilib.command3.Scheduler;
import org.wpilib.command3.Trigger;
import org.wpilib.event.EventLoop;

public final class LoggedNetworkTablesBuilder {

  private LoggedNetworkTablesBuilder() {}

  /**
   * Creates a {@link Trigger} instance that is bound to a boolean value at {@code key} on
   * NetworkTables.
   *
   * @param key the key on NetworkTables
   * @param eventLoop the eventLoop to bind the Trigger to
   * @return a Trigger with a toggle based on a boolean NetworkTables entry
   */
  public static Trigger createLoggedButton(String key, EventLoop eventLoop) {
    LoggedNetworkBoolean loggedNetworkBoolean = new LoggedNetworkBoolean(key, false);

    return new Trigger(Scheduler.getDefault(), eventLoop, loggedNetworkBoolean);
  }

  /**
   * {@code eventLoop} defaults to the default button loop of the {@link Scheduler}
   *
   * @see #createLoggedButton(String, EventLoop)
   */
  public static Trigger createLoggedButton(String key) {
    return createLoggedButton(key, Scheduler.getDefault().getDefaultEventLoop());
  }

  /**
   * Creates a {@link Trigger} that resets its NetworkTables entry to false, after being true for
   * 0.25 seconds.
   *
   * @see #createLoggedButton(String, EventLoop)
   */
  public static Trigger createLoggedAutoResettingButton(String key, EventLoop eventLoop) {
    LoggedNetworkBoolean loggedNetworkBoolean = new LoggedNetworkBoolean(key, false);

    return new Trigger(Scheduler.getDefault(), eventLoop, loggedNetworkBoolean)
        .onTrue(
            Command.noRequirements(
                    coroutine -> {
                      coroutine.wait(Seconds.of(.25));
                      loggedNetworkBoolean.set(false);
                    })
                .named("AutoResettingButton"));
  }

  /**
   * {@code eventLoop} defaults to the default button loop of the {@link Scheduler}
   *
   * @see #createLoggedAutoResettingButton(String, EventLoop)
   */
  public static Trigger createLoggedAutoResettingButton(String key) {
    return createLoggedAutoResettingButton(key, Scheduler.getDefault().getDefaultEventLoop());
  }

  /**
   * Creates a {@link LoggedNetworkChooser} that contains the name of each Enum constant as an
   * option.
   *
   * @param key the key on NetworkTables
   * @param tEnum the class of the Enum
   * @param startingValue the starting Enum value to use on the {@code LoggedNetworkChooser}
   * @param onValueChange a {@link Consumer} that gets called on startup, and whenever the selector
   *     changes with the value it changed to
   * @param <T> the type of the Enum
   * @return the created and published {@code LoggedNetworkChooser}
   */
  public static <T extends Enum<T>> LoggedNetworkChooser<Enum<T>> createSelectorFromEnum(
      String key, Class<T> tEnum, Enum<T> startingValue, Consumer<Enum<T>> onValueChange) {
    LoggedNetworkChooser<Enum<T>> chooser = new LoggedNetworkChooser<>(key);

    chooser.addDefault(startingValue.toString(), startingValue);
    for (Enum<T> value : tEnum.getEnumConstants()) {
      if (value == startingValue) {
        continue;
      }
      chooser.add(value.toString(), value);
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
  public static <T extends Enum<T>> LoggedNetworkChooser<Enum<T>> createSelectorFromEnum(
      String key, Class<T> tEnum, Consumer<Enum<T>> onValueChange) {
    return createSelectorFromEnum(key, tEnum, tEnum.getEnumConstants()[0], onValueChange);
  }
}
