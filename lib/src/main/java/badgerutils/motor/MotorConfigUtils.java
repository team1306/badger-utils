package badgerutils.motor;

import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import edu.wpi.first.units.AngularAccelerationUnit;
import edu.wpi.first.units.measure.AngularAcceleration;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Velocity;

public class MotorConfigUtils {

  /**
   * Reverses the motor direction by changing MotorOutput.Inverted while maintaining all of the
   * other configs
   *
   * @param configToInvert the original config to copy
   * @return a copy of the original configuration with the opposite inverted value
   */
  public static TalonFXConfiguration createInvertedConfig(TalonFXConfiguration configToInvert) {
    return configToInvert
        .clone()
        .withMotorOutput(
            createMotorOutputConfig(
                configToInvert.MotorOutput.Inverted == InvertedValue.Clockwise_Positive
                    ? InvertedValue.CounterClockwise_Positive
                    : InvertedValue.Clockwise_Positive,
                configToInvert.MotorOutput.NeutralMode));
  }

  /**
   * Creates a Slot0Configs object with the desired PID and feedforward gains for a motor. This is
   * the simple version of the method. It does not include the integral gain, acceleration gain, or
   * the slot number.
   *
   * @param kP the PID's proportional gain
   * @param kD the PID's derivative gain
   * @param kS the feedforward's static gain
   * @param kV the feedforward's velocity gain
   * @param kG the feedforward's gravity gain
   * @param gravityType Elevator_Static for elevators; Arm_Cosine for arms;
   * @param staticFeedForwardSign Velocity Sign for velocity PID; Closed-loop sign for position PID
   * @return A Slot0Configs object which can be applied to the motor configurator.
   */
  public static Slot0Configs createSlotConfig(
      double kP,
      double kD,
      double kS,
      double kV,
      double kG,
      GravityTypeValue gravityType,
      StaticFeedforwardSignValue staticFeedForwardSign) {
    return new Slot0Configs()
        .withKP(kP)
        .withKD(kD)
        .withKS(kS)
        .withKV(kV)
        .withKG(kG)
        .withGravityType(gravityType)
        .withStaticFeedforwardSign(staticFeedForwardSign);
  }

  /**
   * Creates a SlotConfigs object with the desired PID and feedforward gains for a motor.
   *
   * @param kP the PID's proportional gain
   * @param kI the PID's integral gain
   * @param kD the PID's derivative gain
   * @param kS the feedforward's static gain
   * @param kV the feedforward's velocity gain
   * @param kG the feedforward's gravity gain
   * @param kA the feedforward's acceleration gain
   * @param gravityType Elevator_Static for elevators; Arm_Cosine for arms;
   * @param staticFeedForwardSign Velocity Sign for velocity PID; Closed-loop sign for position PID
   * @return A SlotConfigs object which can be applied to the motor configurator.
   */
  public static SlotConfigs createSlotConfig(
      int slot,
      double kP,
      double kI,
      double kD,
      double kS,
      double kV,
      double kG,
      double kA,
      GravityTypeValue gravityType,
      StaticFeedforwardSignValue staticFeedForwardSign) {
    final SlotConfigs config =
        new SlotConfigs()
            .withKP(kP)
            .withKI(kI)
            .withKD(kD)
            .withKS(kS)
            .withKV(kV)
            .withKG(kG)
            .withKA(kA)
            .withGravityType(gravityType)
            .withStaticFeedforwardSign(staticFeedForwardSign);
    config.SlotNumber = slot;
    return config;
  }

  /**
   * Creates a feedback config for a remote CANCoder
   *
   * @param canCoder the remote CANCoder
   * @param motorToSensorRatio number of motor turns to turn the CANCoder once
   * @param sensorToMechanismRatio number of CANCoder turns to turn the mechanism once
   * @return A FeedBackConfigs object which can be applied to the motor configurator.
   */
  public static FeedbackConfigs createCanCoderFeedbackConfig(
      CANcoder canCoder, double motorToSensorRatio, double sensorToMechanismRatio) {
    return new FeedbackConfigs()
        .withFeedbackRemoteSensorID(canCoder.getDeviceID())
        .withFeedbackSensorSource(FeedbackSensorSourceValue.RemoteCANcoder)
        .withRotorToSensorRatio(motorToSensorRatio)
        .withSensorToMechanismRatio(sensorToMechanismRatio);
  }

  /**
   * Creates a MotorOutputConfigs object with the desired inverted and idle mode values.
   *
   * @param inverted whether the motor is inverted. Clockwise_Positive is normal;
   *     Counterclockwise_Positive is reversed.
   * @param idleMode how the motor behaves when no voltage is applied. Options are Brake or Coast.
   * @return A MotorOutputConfigs object which can be applied to the motor configurator.
   */
  public static MotorOutputConfigs createMotorOutputConfig(
      InvertedValue inverted, NeutralModeValue idleMode) {
    return new MotorOutputConfigs().withInverted(inverted).withNeutralMode(idleMode);
  }

  /**
   * Creates a CurrentLimitsConfigs object with the desired current limits.
   *
   * @param statorCurrentLimit the output current (amps) of the motor
   * @param supplyCurrentLimit the maximum current (amps) that can be drawn from the battery
   * @return A CurrentLimitsConfigs object which can be applied to the motor configurator.
   */
  public static CurrentLimitsConfigs createCurrentLimitsConfig(
      Current statorCurrentLimit, Current supplyCurrentLimit) {
    return new CurrentLimitsConfigs()
        .withStatorCurrentLimitEnable(true)
        .withSupplyCurrentLimitEnable(true)
        .withStatorCurrentLimit(statorCurrentLimit)
        .withSupplyCurrentLimit(supplyCurrentLimit);
  }

  /**
   * Creates a MotionMagicConfigs object with the desired constraints.
   *
   * @param maxVelocity The maximum velocity of the system (units/s)
   * @param maxAcceleration The maximum Acceleration of the system (units/s^2)
   * @param maxJerk The maximum Jerk of the system (units/s^3)
   * @return A MotionMagicConfigs object which can be applied to the motor configurator.
   */
  public static MotionMagicConfigs createMotionMagicConfig(
      AngularVelocity maxVelocity,
      AngularAcceleration maxAcceleration,
      Velocity<AngularAccelerationUnit> maxJerk) {
    return createMotionMagicConfig(maxVelocity, maxAcceleration).withMotionMagicJerk(maxJerk);
  }

  /**
   * Creates a MotionMagicConfigs object with the desired constraints.
   *
   * @param maxVelocity The maximum velocity of the system (units/s)
   * @param maxAcceleration The maximum Acceleration of the system (units/s/s)
   * @return A MotionMagicConfigs object which can be applied to the motor configurator.
   */
  public static MotionMagicConfigs createMotionMagicConfig(
      AngularVelocity maxVelocity, AngularAcceleration maxAcceleration) {
    return new MotionMagicConfigs()
        .withMotionMagicCruiseVelocity(maxVelocity)
        .withMotionMagicAcceleration(maxAcceleration);
  }
}
