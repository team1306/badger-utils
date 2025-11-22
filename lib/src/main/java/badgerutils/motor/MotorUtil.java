package badgerutils.motor;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public class MotorUtil {
    public static TalonFX initTalonFX(int motorId, NeutralModeValue idleMode) {
        return initTalonFX(motorId, idleMode, InvertedValue.Clockwise_Positive);
    }

    public static TalonFX initTalonFX(int motorId, NeutralModeValue idleMode, InvertedValue inverted) {
        final TalonFX motor = new TalonFX(motorId);
        final TalonFXConfigurator configurator = motor.getConfigurator();

        final MotorOutputConfigs motorOutputConfig = new MotorOutputConfigs();

        motorOutputConfig.Inverted = inverted;
        motorOutputConfig.NeutralMode = idleMode;

        configurator.apply(motorOutputConfig);

        return motor;
    }

    public static TalonFX initTalonFX(int motorId, MotorOutputConfigs motorOutputConfigs, Slot0Configs pidConfigBuilder, CurrentLimitsConfigs currentLimitsConfigs) {
        final TalonFX motor = new TalonFX(motorId);
        TalonFXConfigurator configurator = motor.getConfigurator();

        return motor;
    }

    public static Slot0Configs pidConfigBuilder(double kP, double kI, double kD, double kS, double kV, double kG, double kA) {
        Slot0Configs config = new Slot0Configs();
        config.kP = kP;
        config.kI = kI;
        config.kD = kD;
        config.kS = kS;
        config.kV = kV;
        config.kG = kG;
        config.kA = kA;
        return config;
    }

    public static MotorOutputConfigs motorOutputConfigBuilder(InvertedValue inverted, NeutralModeValue idleMode) {
        MotorOutputConfigs config = new MotorOutputConfigs();
        config.Inverted = inverted;
        config.NeutralMode = idleMode;
        return config;
    }

    public static CurrentLimitsConfigs currentLimitsConfigBuilder(double statorCurrentLimit, double supplyCurrentLimit) {
        CurrentLimitsConfigs config = new CurrentLimitsConfigs();
        config.StatorCurrentLimit = statorCurrentLimit;
        config.SupplyCurrentLimit = supplyCurrentLimit;
        return config;
    }
}