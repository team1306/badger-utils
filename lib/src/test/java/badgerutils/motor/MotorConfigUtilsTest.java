package badgerutils.motor;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecondPerSecond;
import static edu.wpi.first.units.Units.Second;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import org.junit.jupiter.api.Test;

class MotorConfigUtilsTest {
  private static final double DELTA = 1e-9;

  @Test
  void createsSimpleSlotConfiguration() {
    Slot0Configs config =
        MotorConfigUtils.createSlotConfig(
            1.0,
            2.0,
            3.0,
            4.0,
            5.0,
            GravityTypeValue.Arm_Cosine,
            StaticFeedforwardSignValue.UseClosedLoopSign);

    assertEquals(1.0, config.kP, DELTA);
    assertEquals(0.0, config.kI, DELTA);
    assertEquals(2.0, config.kD, DELTA);
    assertEquals(3.0, config.kS, DELTA);
    assertEquals(4.0, config.kV, DELTA);
    assertEquals(0.0, config.kA, DELTA);
    assertEquals(5.0, config.kG, DELTA);
    assertEquals(GravityTypeValue.Arm_Cosine, config.GravityType);
    assertEquals(StaticFeedforwardSignValue.UseClosedLoopSign, config.StaticFeedforwardSign);
  }

  @Test
  void createsFullSlotConfiguration() {
    SlotConfigs config =
        MotorConfigUtils.createSlotConfig(
            2,
            1.0,
            2.0,
            3.0,
            4.0,
            5.0,
            6.0,
            7.0,
            GravityTypeValue.Elevator_Static,
            StaticFeedforwardSignValue.UseVelocitySign);

    assertEquals(2, config.SlotNumber);
    assertEquals(1.0, config.kP, DELTA);
    assertEquals(2.0, config.kI, DELTA);
    assertEquals(3.0, config.kD, DELTA);
    assertEquals(4.0, config.kS, DELTA);
    assertEquals(5.0, config.kV, DELTA);
    assertEquals(6.0, config.kG, DELTA);
    assertEquals(7.0, config.kA, DELTA);
    assertEquals(GravityTypeValue.Elevator_Static, config.GravityType);
    assertEquals(StaticFeedforwardSignValue.UseVelocitySign, config.StaticFeedforwardSign);
  }

  @Test
  void createsMotorOutputConfiguration() {
    MotorOutputConfigs config =
        MotorConfigUtils.createMotorOutputConfig(
            InvertedValue.Clockwise_Positive, NeutralModeValue.Brake);

    assertEquals(InvertedValue.Clockwise_Positive, config.Inverted);
    assertEquals(NeutralModeValue.Brake, config.NeutralMode);
  }

  @Test
  void createsEnabledCurrentLimitsUsingMeasureValues() {
    CurrentLimitsConfigs config =
        MotorConfigUtils.createCurrentLimitsConfig(Amps.of(80.0), Amps.of(35.0));

    assertTrue(config.StatorCurrentLimitEnable);
    assertTrue(config.SupplyCurrentLimitEnable);
    assertEquals(80.0, config.StatorCurrentLimit, DELTA);
    assertEquals(35.0, config.SupplyCurrentLimit, DELTA);
  }

  @Test
  void createsMotionMagicConfigurationWithoutJerk() {
    MotionMagicConfigs config =
        MotorConfigUtils.createMotionMagicConfig(
            RotationsPerSecond.of(12.0), RotationsPerSecondPerSecond.of(34.0));

    assertEquals(12.0, config.MotionMagicCruiseVelocity, DELTA);
    assertEquals(34.0, config.MotionMagicAcceleration, DELTA);
    assertEquals(0.0, config.MotionMagicJerk, DELTA);
  }

  @Test
  void createsMotionMagicConfigurationWithJerk() {
    MotionMagicConfigs config =
        MotorConfigUtils.createMotionMagicConfig(
            RotationsPerSecond.of(12.0),
            RotationsPerSecondPerSecond.of(34.0),
            RotationsPerSecondPerSecond.per(Second).of(56.0));

    assertEquals(12.0, config.MotionMagicCruiseVelocity, DELTA);
    assertEquals(34.0, config.MotionMagicAcceleration, DELTA);
    assertEquals(56.0, config.MotionMagicJerk, DELTA);
  }

  @Test
  void invertedConfigurationIsAnIndependentCopy() {
    TalonFXConfiguration original = new TalonFXConfiguration();
    original.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    original.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    original.CurrentLimits.SupplyCurrentLimit = 31.0;

    TalonFXConfiguration inverted = MotorConfigUtils.createInvertedConfig(original);

    assertNotSame(original, inverted);
    assertNotSame(original.CurrentLimits, inverted.CurrentLimits);
    assertEquals(InvertedValue.CounterClockwise_Positive, inverted.MotorOutput.Inverted);
    assertEquals(NeutralModeValue.Brake, inverted.MotorOutput.NeutralMode);
    assertEquals(31.0, inverted.CurrentLimits.SupplyCurrentLimit, DELTA);
    assertEquals(InvertedValue.Clockwise_Positive, original.MotorOutput.Inverted);

    inverted.CurrentLimits.SupplyCurrentLimit = 10.0;
    assertEquals(31.0, original.CurrentLimits.SupplyCurrentLimit, DELTA);
  }

  @Test
  void invertedConfigurationFlipsCounterClockwiseToClockwise() {
    TalonFXConfiguration original = new TalonFXConfiguration();
    original.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    TalonFXConfiguration inverted = MotorConfigUtils.createInvertedConfig(original);

    assertEquals(InvertedValue.Clockwise_Positive, inverted.MotorOutput.Inverted);
    assertNotSame(original, inverted);
  }
}
