package badgerutils.statemachine;

/**
 * Represents a function transitioning from one state to another
 *
 * @param <T> enum type
 */
@FunctionalInterface
public interface StateEdge<T extends Enum<T>> {
    /**
     * Executes a function that is run whenever the state changes. 
     * @param transition the transition that the edge is being executed at
     */
    void performTransition(Transition<T> transition);
}
