package badgerutils.subsystem;

/**
 * Represents a function transitioning from one state to another
 *
 * @param <T> enum type
 */
@FunctionalInterface
public interface StateEdge<T extends Enum<T>> {
    void performTransition(Transition<T> transition);
}
