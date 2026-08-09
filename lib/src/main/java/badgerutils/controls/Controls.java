package badgerutils.controls;

import java.util.Map;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;
import org.wpilib.driverstation.internal.DriverStationBackend;

public class Controls<E extends Enum<E>> {
  private ControllerMapping currentState;

  public Controls(Map<E, ControllerMapping> mappings, E defaultMapping) {
    DriverStationBackend.silenceJoystickConnectionAlert(true);

    LoggedDashboardChooser<ControllerMapping> chooser =
        new LoggedDashboardChooser<>("Controls/Controller Mode");

    mappings.forEach(
        (key, value) -> {
          if (key == defaultMapping) {
            chooser.addDefaultOption(defaultMapping.name(), value);
          } else {
            chooser.addOption(key.name(), value);
          }
        });

    chooser.onChange(
        nextState -> {
          if (currentState != null) {
            currentState.clear();
          }
          nextState.bind();

          currentState = nextState;
        });
  }
}
