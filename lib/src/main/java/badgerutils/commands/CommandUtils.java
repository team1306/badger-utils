package badgerutils.commands;

import java.util.Optional;
import java.util.function.Consumer;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.command3.Scheduler;

public final class CommandUtils {

  private CommandUtils() {}

  public static void removeAndCancelDefaultCommand(Mechanism mechanism) {
    runIfNotNull(
        mechanism.getDefaultCommand(),
        (Command command) -> {
          mechanism.setDefaultCommand(null);
          Scheduler.getDefault().cancel(command);
        });
  }

  /**
   * Run consumer if object is not null, else do nothing
   *
   * @param <T> type of object
   * @param object input object
   * @param objectConsumer consumer to apply to object
   * @return returns optional input object
   */
  public static <T> Optional<T> runIfNotNull(T object, Consumer<T> objectConsumer) {
    if (object != null) {
      objectConsumer.accept(object);
    }
    return Optional.ofNullable(object);
  }
}
