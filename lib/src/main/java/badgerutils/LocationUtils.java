package badgerutils;

import static org.wpilib.units.Units.Meters;

import java.util.Arrays;
import java.util.Optional;

import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.geometry.Translation2d;
import org.wpilib.units.measure.Distance;

public class LocationUtils {
  /**
   * Returns the angle between two points. If both points are the same, a Rotation2d representing zero degrees is returned
   *
   * @param startPosition the first point
   * @param endPosition the second point
   * @return A Rotation2d containing the angle from the first point to the second point in relation
   *     to the positive X axis.
   */
  public static Rotation2d getDirectionToLocation(
      Translation2d startPosition, Translation2d endPosition) {
      Translation2d difference = endPosition.minus(startPosition);
    return difference.getAngle().orElse(Rotation2d.kZero);
  }

  /**
   * Gets the closest location from a provided list
   *
   * @param startLocation the location to compare the others with
   * @param locations an array of locations to pick the nearest from
   * @return a translation2d representing the nearest location to the start location
   */
  public static Translation2d getClosestLocation(
      Translation2d startLocation, Translation2d... locations) {
    return startLocation.nearest(Arrays.asList(locations));
  }

  /**
   * Returns the distance between two translation2ds
   *
   * @param position1 the first point
   * @param position2 the second point
   * @return A distance containing the distance between the two points assuming one unit is one
   *     meter.
   */
  public static Distance getDistanceToLocation(Translation2d position1, Translation2d position2) {
    return Meters.of(position1.getDistance(position2));
  }
}
