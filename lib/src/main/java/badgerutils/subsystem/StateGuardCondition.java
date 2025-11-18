package badgerutils.subsystem;

/**
 * Represents a condition enabling or blocking the change between two states
 *
 * @param <T> enum type
 */
@FunctionalInterface
public interface StateGuardCondition<T extends Enum<T>> {
    boolean canChange(Transition<T> transition);
}
