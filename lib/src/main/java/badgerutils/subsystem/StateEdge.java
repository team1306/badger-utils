package badgerutils.subsystem;

/**
 * Represents a function transitioning from one state to another
 *
 * @param <T> enum type
 */
@FunctionalInterface
public interface StateEdge<T extends Enum<T>> {
    /**
     * Performs a transition between two states. Can be an operation that changes robot state
     */
    void performTransition(T currentState, T nextState);
}
