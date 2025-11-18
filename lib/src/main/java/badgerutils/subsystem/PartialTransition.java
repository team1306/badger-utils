package badgerutils.subsystem;

import java.util.Set;

/**
 * This should not be created by any implementation of the {@link StateMachine}. It is used internally to match states
 */
public record PartialTransition<T extends Enum<T>>(Set<T> currentStates, Set<T> nextStates) {
    public PartialTransition {
        currentStates = Set.copyOf(currentStates);
        nextStates = Set.copyOf(nextStates);
    }

    public boolean matches(T currentState, T nextState) {
        if (currentStates.isEmpty()) {
            if (nextStates.isEmpty()) {
                return true;
            } else {
                return nextStates.contains(currentState);
            }
        } else {
            if (nextStates.isEmpty()) {
                return currentStates.contains(currentState);
            } else {
                return currentStates.contains(currentState) && nextStates.contains(nextState);
            }
        }

    }
}
