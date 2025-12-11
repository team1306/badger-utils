package badgerutils.motor;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class MotorConfigBuilders {
    /** Creates a Slot0Configs object with the desired PID and feedforward gains for a motor.
     * @param kP the PID's proportional gain
     * @param kI the PID's integral gain
     * @param kD the PID's derivative gain
     * @param kS the feedforward's static gain
     * @param kV the feedforward's velocity gain
     * @param kG the feedforward's gravity gain
     * @param kA the feedforward's acceleration gain
     * @return A Slot0Configs object which can be applied to the motor configurator.
     */
    public static Slot0Configs pidConfigBuilder(double kP, double kI, double kD, double kS, double kV, double kG, double kA, GravityTypeValue gravityType) {
        Slot0Configs config = new Slot0Configs();
        config.kP = kP;
        config.kI = kI;
        config.kD = kD;
        config.kS = kS;
        config.kV = kV;
        config.kG = kG;
        config.kA = kA;
        config.GravityType = gravityType;
        return config;
    }

    /**
     * Creates a MotorOutputConfigs object with the desired inverted and idle mode values.
     * @param inverted whether the motor is inverted. Clockwise_Positive is normal; Counterclockwise_Positive is reversed.
     * @param idleMode how the motor behaves when no voltage is applied. Options are Brake or Coast.
     * @return A MotorOutputConfigs object which can be applied to the motor configurator.
     */
    public static MotorOutputConfigs motorOutputConfigBuilder(InvertedValue inverted, NeutralModeValue idleMode) {
        MotorOutputConfigs config = new MotorOutputConfigs();
        config.Inverted = inverted;
        config.NeutralMode = idleMode;
        return config;
    }

    /**
     * Creates a CurrentLimitsConfigs object with the desired current limits.
     * @param statorCurrentLimit the output current (amps) of the motor
     * @param supplyCurrentLimit the maximum current (amps) that can be drawn from the battery
     * @return A CurrentLimitsConfigs object which can be applied to the motor configurator.
     */
    public static CurrentLimitsConfigs currentLimitsConfigBuilder(double statorCurrentLimit, double supplyCurrentLimit) {
        CurrentLimitsConfigs config = new CurrentLimitsConfigs();
        config.StatorCurrentLimit = statorCurrentLimit;
        config.SupplyCurrentLimit = supplyCurrentLimit;
        return config;
    }

    /**
     * Creates a MotionMagicConfigs object with the desired constraints.
     * @param maxVelocity The maximum velocity of the system (units/s)
     * @param maxAcceleration The maximum Acceleration of the system (units/s/s)
     * @return A MotionMagicConfigs object which can be applied to the motor configurator.
     */
    public static MotionMagicConfigs motionMagicConfigBuilder(double maxVelocity, double maxAcceleration) {
        MotionMagicConfigs config = new MotionMagicConfigs();
        config.MotionMagicCruiseVelocity = maxVelocity;
        config.MotionMagicAcceleration = maxAcceleration;
        return config;
    }
}
