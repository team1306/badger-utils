package badgerutils.subsystem;

/**
 * Represents a condition enabling or blocking the change between two states
 *
 * @param <T> enum type
 */
@FunctionalInterface
public interface StateGuardCondition<T extends Enum<T>> {
    /**
     * Checks whether the transition is valid and can change to {@code nextState} in the transition
     * @param transition the transition that the guard is being executed at 
     * @return whether the state can switch to the {@code nextState} of the transition
     */
    boolean canChange(Transition<T> transition);
}
