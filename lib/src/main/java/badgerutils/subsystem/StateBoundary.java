package badgerutils.subsystem;

/**
 * Represents a condition enabling or blocking the change between two states
 *
 * @param <T> enum type
 */
@FunctionalInterface
public interface StateBoundary<T extends Enum<T>> {
    /**
     * Decides whether the state change from the current state given the next state. Cannot be an operation that changes robot state.
     *
     * @param currentState the current state
     * @param toState the next state to go to
     *
     * @return whether the state can change
     */
    boolean canChange(T currentState, T toState);
}
