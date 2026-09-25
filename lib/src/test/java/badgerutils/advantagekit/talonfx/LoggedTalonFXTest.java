package badgerutils.advantagekit.talonfx;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Celsius;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecondPerSecond;
import static edu.wpi.first.units.Units.Volts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.ctre.phoenix6.controls.NeutralOut;
import org.junit.jupiter.api.Test;

class LoggedTalonFXTest {
  private static final double DELTA = 1e-9;

  @Test
  void measureConstructorConvertsValuesToRecordUnits() {
    NeutralOut control = new NeutralOut();
    LoggedTalonFX logged =
        new LoggedTalonFX(
            4,
            false,
            RPM.of(180.0),
            Degrees.of(90.0),
            RotationsPerSecondPerSecond.of(6.0),
            Celsius.of(42.0),
            Amps.of(30.0),
            Amps.of(40.0),
            Volts.of(11.5),
            -0.2,
            3.5,
            control);

    assertEquals(4, logged.id());
    assertFalse(logged.isMotorConnected());
    assertEquals(3.0, logged.velocity(), DELTA);
    assertEquals(0.25, logged.position(), DELTA);
    assertEquals(6.0, logged.acceleration(), DELTA);
    assertEquals(42.0, logged.temp(), DELTA);
    assertEquals(30.0, logged.supplyCurrent(), DELTA);
    assertEquals(40.0, logged.statorCurrent(), DELTA);
    assertEquals(11.5, logged.voltage(), DELTA);
    assertEquals(-0.2, logged.closedLoopError(), DELTA);
    assertEquals(3.5, logged.closedLoopTarget(), DELTA);
    assertEquals(control.toString(), logged.controlInfo());
  }

  @Test
  void gettersReturnTypedMeasures() {
    LoggedTalonFX logged =
        new LoggedTalonFX(1, true, 2.0, 3.0, 4.0, 50.0, 6.0, 7.0, 8.0, 9.0, 10.0, "control");

    assertEquals(2.0, logged.getVelocity().in(RotationsPerSecond), DELTA);
    assertEquals(3.0, logged.getPosition().in(Rotations), DELTA);
    assertEquals(4.0, logged.getAcceleration().in(RotationsPerSecondPerSecond), DELTA);
    assertEquals(50.0, logged.getTemp().in(Celsius), DELTA);
    assertEquals(6.0, logged.getSupplyCurrent().in(Amps), DELTA);
    assertEquals(7.0, logged.getStatorCurrent().in(Amps), DELTA);
    assertEquals(8.0, logged.getVoltage().in(Volts), DELTA);
  }
}
