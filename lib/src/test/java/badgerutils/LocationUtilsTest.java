package badgerutils;

import static edu.wpi.first.units.Units.Meters;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.measure.Distance;

public class LocationUtilsTest {
  private static final double DELTA = 1e-6;

  @Test
  void getDirectionToLocation() {
    Translation2d start = new Translation2d(1, 2);
    Translation2d end = new Translation2d(2, 3);

    Rotation2d result = LocationUtils.getDirectionToLocation(start, end);

    assertEquals(result.getDegrees(), 45, DELTA);

    end = new Translation2d(1, 3);
    result = LocationUtils.getDirectionToLocation(start, end);
    assertEquals(result.getDegrees(), 90, DELTA);
  }

  @Test
  void getClosestLocation() {
    Translation2d start = new Translation2d(1, 2);
    Translation2d loc1 = new Translation2d(2, 2);
    Translation2d loc2 = new Translation2d(3, 3);
    Translation2d loc3 = new Translation2d(0, 0);

    Translation2d closest = LocationUtils.getClosestLocation(start, loc1, loc2, loc3);

    assertEquals(closest, loc1);
  }

  @Test
  void getDistanceToLocation() {
    Translation2d pos1 = new Translation2d(1, 2);
    Translation2d pos2 = new Translation2d(4, 6);

    Distance distance = LocationUtils.getDistanceToLocation(pos1, pos2);

    assertEquals(distance.in(Meters), 5, DELTA);
  }
}
