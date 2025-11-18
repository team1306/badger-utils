package badgerutils.subsystem;


/**
 * Represents a transition between two states.
 *
 * @param currentState the current state of a system
 * @param nextState the potential next state of a system
 * @param <T> the enum type
 */
public record Transition<T extends Enum<T>>(T currentState, T nextState) {

    /**
     * {@return whether the transition is a valid transition or not}
     */
    public boolean isValid(){
        return !currentState.equals(nextState);
    }
}
