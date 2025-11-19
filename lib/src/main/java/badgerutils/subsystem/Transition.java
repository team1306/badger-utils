package badgerutils.subsystem;


/**
 * Represents a transition between two states.
 *
 * @param previousState the previous state of a system
 * @param nextState the next state of a system
 * @param <T> the enum type
 */
public record Transition<T extends Enum<T>>(T previousState, T nextState) {

    /**
     * {@return whether the transition is a valid transition or not}
     */
    public boolean isValid(){
        return !previousState.equals(nextState);
    }
}
