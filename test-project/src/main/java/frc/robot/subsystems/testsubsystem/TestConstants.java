package frc.robot.subsystems.testsubsystem;

import static edu.wpi.first.units.Units.Amps;

import badgerutils.motor.MotorConfigUtils;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class TestConstants {
  public static final double KP = 0.1;
  public static final double KD = 0.01;
  public static final double KS = 0.2;
  public static final double KV = 0.5;

  public static final TalonFXConfiguration CW_CONFIG =
      new TalonFXConfiguration()
          .withMotorOutput(
              MotorConfigUtils.createMotorOutputConfig(
                  InvertedValue.Clockwise_Positive, NeutralModeValue.Coast))
          .withCurrentLimits(MotorConfigUtils.createCurrentLimitsConfig(Amps.of(60), Amps.of(40)))
          .withSlot0(
              MotorConfigUtils.createSlotConfig(
                  KP, KD, KS, KV, 0, GravityTypeValue.Elevator_Static));
}
