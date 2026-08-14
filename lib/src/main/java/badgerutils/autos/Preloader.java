package badgerutils.autos;

import java.util.HashMap;
import java.util.Map;
import org.wpilib.command3.Command;

public class Preloader<T extends Enum<T>> {
  private final Map<T, Command> preloadedCommands = new HashMap<>();
  private Class<T> type = null;

  public void addPreloadedCommand(T key, Command command) {
    preloadedCommands.put(key, command);
    if (type == null) {
      type = key.getDeclaringClass();
    }
  }

  public boolean validateAllCommandsExists() {
    for (T key : type.getEnumConstants()) {
      if (!preloadedCommands.containsKey(key)) {
        return false;
      }
    }
    return true;
  }

  public Command getPreloadedCommand(T key) {
    return preloadedCommands.get(key);
  }
}
