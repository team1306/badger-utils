package badgerutils.advantagekit.cancoder;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class LoggedCANCoderTest {
  private static final double DELTA = 1e-9;

  @Test
  void measureConstructorConvertsValuesToRecordUnits() {
    LoggedCANCoder logged =
        new LoggedCANCoder(7, false, RPM.of(120.0), Degrees.of(180.0), Degrees.of(90.0));

    assertEquals(7, logged.id());
    assertFalse(logged.isConnected());
    assertEquals(2.0, logged.velocity(), DELTA);
    assertEquals(0.5, logged.position(), DELTA);
    assertEquals(0.25, logged.absolutePosition(), DELTA);
  }

  @Test
  void gettersReturnTypedMeasures() {
    LoggedCANCoder logged = new LoggedCANCoder(3, true, 1.25, -2.5, 0.75);

    assertEquals(1.25, logged.getVelocity().in(RotationsPerSecond), DELTA);
    assertEquals(-2.5, logged.getPosition().in(Rotations), DELTA);
    assertEquals(0.75, logged.getAbsolutePosition().in(Rotations), DELTA);
  }
}
