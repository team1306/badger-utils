package badgerutils.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Optional;
import java.util.function.Consumer;

public final class CommandUtils {
    
    private CommandUtils() {}
    
    public static void removeAndCancelDefaultCommand(Subsystem subsystem) {
        runIfNotNull(subsystem.getDefaultCommand(), (Command command) -> {
            subsystem.removeDefaultCommand();
            command.cancel();
        });
    }

    /**
     * Run consumer if object is not null, else do nothing
     *
     * @param <T> type of object
     * @param object input object
     * @param objectConsumer consumer to apply to object
     *
     * @return returns optional input object
     */
    public static <T> Optional<T> runIfNotNull(T object, Consumer<T> objectConsumer) {
        if (object != null) {
            objectConsumer.accept(object);
        }
        return Optional.ofNullable(object);
    }
}
