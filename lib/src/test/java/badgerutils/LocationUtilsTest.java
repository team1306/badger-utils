package badgerutils;

import static edu.wpi.first.units.Units.Meters;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.measure.Distance;
import org.junit.jupiter.api.Test;

class LocationUtilsTest {
  private static final double DELTA = 1e-6;

  @Test
  void getDirectionToLocation() {
    Translation2d start = new Translation2d(1, 2);
    Translation2d end = new Translation2d(2, 3);

    Rotation2d result = LocationUtils.getDirectionToLocation(start, end);

    assertEquals(45, result.getDegrees(), DELTA);

    end = new Translation2d(1, 3);
    result = LocationUtils.getDirectionToLocation(start, end);
    assertEquals(90, result.getDegrees(), DELTA);

    end = new Translation2d(0, 2);
    result = LocationUtils.getDirectionToLocation(start, end);
    assertEquals(180, Math.abs(result.getDegrees()), DELTA);
  }

  @Test
  void getClosestLocation() {
    Translation2d start = new Translation2d(1, 2);
    Translation2d loc1 = new Translation2d(2, 2);
    Translation2d loc2 = new Translation2d(3, 3);
    Translation2d loc3 = new Translation2d(0, 0);

    Translation2d closest = LocationUtils.getClosestLocation(start, loc1, loc2, loc3);

    assertSame(loc1, closest);

    closest = LocationUtils.getClosestLocation(start, loc3, loc2, loc1);
    assertSame(loc1, closest);
  }

  @Test
  void getDistanceToLocation() {
    Translation2d pos1 = new Translation2d(1, 2);
    Translation2d pos2 = new Translation2d(4, 6);

    Distance distance = LocationUtils.getDistanceToLocation(pos1, pos2);

    assertEquals(5, distance.in(Meters), DELTA);

    assertEquals(5, LocationUtils.getDistanceToLocation(pos2, pos1).in(Meters), DELTA);
  }
}
