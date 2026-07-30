package badgerutils.controls;

import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/** An abstract class that can be extedned to define custom controller mappings. */
public abstract class ControllerMapping {
  protected CommandXboxController driverController;
  protected CommandXboxController operatorController;

  public static final EventLoop CONTROLS_EVENT_LOOP = new EventLoop();

  /**
   * Creates a new ControllerMapping object.
   *
   * @param driverController The driver controller.
   * @param operatorController The operator controller.
   */
  public ControllerMapping(
      CommandXboxController driverController, CommandXboxController operatorController) {
    this.driverController = driverController;
    this.operatorController = operatorController;
  }

  /** Bind the controller buttons to commands. */
  public abstract void bind();

  /**
   * Clears all button bindings from the controller mapping. Also override this method to remove and
   * cancel any default commands.
   */
  public void clear() {
    CONTROLS_EVENT_LOOP.clear();
  }
}
