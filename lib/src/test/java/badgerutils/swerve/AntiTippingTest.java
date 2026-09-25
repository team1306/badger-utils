package badgerutils.swerve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class AntiTippingTest {
  private static final double DELTA = 1e-9;

  @Test
  void calculateUpdatesMeasurementsAndCorrection() {
    AtomicReference<Double> pitch = new AtomicReference<>(3.0);
    AtomicReference<Double> roll = new AtomicReference<>(4.0);
    AntiTipping antiTipping = new AntiTipping(pitch::get, roll::get, 0.2, 2.0, 10.0);

    antiTipping.calculate();

    assertEquals(3.0, antiTipping.getPitch(), DELTA);
    assertEquals(4.0, antiTipping.getRoll(), DELTA);
    assertEquals(5.0, antiTipping.getInclinationMagnitude(), DELTA);
    assertTrue(antiTipping.isTipping());
    assertEquals(Math.toDegrees(Math.atan2(-4.0, -3.0)), antiTipping.getYawDirectionDeg(), DELTA);
    assertEquals(-0.8, antiTipping.getSpeeds().vxMetersPerSecond, DELTA);
    assertEquals(-0.6, antiTipping.getSpeeds().vyMetersPerSecond, DELTA);
    assertEquals(0.0, antiTipping.getSpeeds().omegaRadiansPerSecond, DELTA);
  }

  @Test
  void correctionSpeedIsClamped() {
    AntiTipping antiTipping = new AntiTipping(() -> 30.0, () -> 40.0, 1.0, 5.0, 2.0);

    antiTipping.calculate();

    assertEquals(-1.6, antiTipping.getSpeeds().vxMetersPerSecond, DELTA);
    assertEquals(-1.2, antiTipping.getSpeeds().vyMetersPerSecond, DELTA);
  }

  @Test
  void thresholdUsesStrictComparisonAndSettersTakeEffect() {
    AtomicReference<Double> pitch = new AtomicReference<>(5.0);
    AtomicReference<Double> roll = new AtomicReference<>(0.0);
    AntiTipping antiTipping = new AntiTipping(pitch::get, roll::get, 1.0, 5.0, 10.0);

    antiTipping.calculate();
    assertFalse(antiTipping.isTipping());

    antiTipping.setTippingThresholdDegrees(4.9);
    antiTipping.setMaxCorrectionSpeed(1.0);
    antiTipping.calculate();

    assertTrue(antiTipping.isTipping());
    assertEquals(0.0, antiTipping.getSpeeds().vxMetersPerSecond, DELTA);
    assertEquals(-1.0, antiTipping.getSpeeds().vyMetersPerSecond, DELTA);
  }

  @Test
  void zeroInclinationProducesNoCorrection() {
    AntiTipping antiTipping = new AntiTipping(() -> 0.0, () -> 0.0, 1.0, 5.0, 2.0);

    antiTipping.calculate();

    assertFalse(antiTipping.isTipping());
    assertEquals(0.0, antiTipping.getInclinationMagnitude(), DELTA);
    assertEquals(0.0, antiTipping.getSpeeds().vxMetersPerSecond, DELTA);
    assertEquals(0.0, antiTipping.getSpeeds().vyMetersPerSecond, DELTA);
  }
}
