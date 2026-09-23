package frc.robot.subsystems.testsubsystem;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecondPerSecond;

import badgerutils.motor.MotorConfigUtils;
import edu.wpi.first.units.measure.AngularAcceleration;
import edu.wpi.first.units.measure.AngularVelocity;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

public class TestConstants {
  public static final double KP = 0.1;
  public static final double KD = 0.01;
  public static final double KS = 0.2;
  public static final double KV = 0.5;

  public static final AngularVelocity MAX_VELOCITY = RotationsPerSecond.of(10);
  public static final AngularAcceleration MAX_ACCELERATION = RotationsPerSecondPerSecond.of(10);

  public static final TalonFXConfiguration CW_CONFIG =
      new TalonFXConfiguration()
          .withMotorOutput(
              MotorConfigUtils.createMotorOutputConfig(
                  InvertedValue.Clockwise_Positive, NeutralModeValue.Coast))
          .withCurrentLimits(MotorConfigUtils.createCurrentLimitsConfig(Amps.of(60), Amps.of(40)))
          .withSlot0(
              MotorConfigUtils.createSlotConfig(
                  KP,
                  KD,
                  KS,
                  KV,
                  0,
                  GravityTypeValue.Elevator_Static,
                  StaticFeedforwardSignValue.UseVelocitySign))
            .withMotionMagic(MotorConfigUtils.createMotionMagicConfig(MAX_VELOCITY, MAX_ACCELERATION));
}
