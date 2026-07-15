package badgerutils.advantagekit;

import badgerutils.motor.MotorConfigUtils;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import org.littletonrobotics.junction.AutoLogOutput;

/**
 * A class that automatically adds PID values to the NetworkTables and updates the motors when they
 * are changed.
 */
public class PIDTunable {
  @AutoLogOutput private final LoggedNetworkNumberPlus kPSupplier;
  @AutoLogOutput private final LoggedNetworkNumberPlus kDSupplier;
  @AutoLogOutput private final LoggedNetworkNumberPlus kSSupplier;
  @AutoLogOutput private final LoggedNetworkNumberPlus kVSupplier;
  @AutoLogOutput private final LoggedNetworkNumberPlus kGSupplier;
  @AutoLogOutput private final LoggedNetworkNumberPlus kASupplier;
  @AutoLogOutput private final LoggedNetworkNumberPlus kISupplier;

  private final TalonFX[] motors;

  private final SlotConfigs defaultConfig;

  /**
   * Creates a new PIDTunable object that automatically adds the PID values to the NetworkTables and
   * updates the motors when they are changed.
   *
   * @param subsystemName The name of the subsystem that the Tunables will be logged under.
   * @param defaultConfig The default PID configuration.
   * @param motors The motors to update when the values are changed.
   */
  public PIDTunable(String subsystemName, SlotConfigs defaultConfig, TalonFX... motors) {
    this.kPSupplier =
        new LoggedNetworkNumberPlus("Tuning/" + subsystemName + "/kP", defaultConfig.kP);
    this.kDSupplier =
        new LoggedNetworkNumberPlus("Tuning/" + subsystemName + "/kD", defaultConfig.kD);
    this.kSSupplier =
        new LoggedNetworkNumberPlus("Tuning/" + subsystemName + "/kS", defaultConfig.kS);
    this.kVSupplier =
        new LoggedNetworkNumberPlus("Tuning/" + subsystemName + "/kV", defaultConfig.kV);
    this.kGSupplier =
        new LoggedNetworkNumberPlus("Tuning/" + subsystemName + "/kG", defaultConfig.kG);
    this.kASupplier =
        new LoggedNetworkNumberPlus("Tuning/" + subsystemName + "/kA", defaultConfig.kA);
    this.kISupplier =
        new LoggedNetworkNumberPlus("Tuning/" + subsystemName + "/kI", defaultConfig.kI);
    this.motors = motors;
    this.defaultConfig = defaultConfig;

    kPSupplier.addSubscriber(value -> updatePID());
    kDSupplier.addSubscriber(value -> updatePID());
    kSSupplier.addSubscriber(value -> updatePID());
    kVSupplier.addSubscriber(value -> updatePID());
    kGSupplier.addSubscriber(value -> updatePID());
    kASupplier.addSubscriber(value -> updatePID());
    kISupplier.addSubscriber(value -> updatePID());
  }

  private void updatePID() {
    SlotConfigs config =
        MotorConfigUtils.createSlotConfig(
            defaultConfig.SlotNumber,
            kPSupplier.get(),
            kISupplier.get(),
            kDSupplier.get(),
            kSSupplier.get(),
            kVSupplier.get(),
            kGSupplier.get(),
            kASupplier.get(),
            defaultConfig.GravityType);
    config.SlotNumber = defaultConfig.SlotNumber;

    for (TalonFX motor : motors) {
      motor.getConfigurator().apply(config);
    }
  }
}
