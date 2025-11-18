package badgerutils.subsystem;

import java.util.List;

/**
 * Represents a transition between two states. There are several rules to expand opportunities of possible states. An empty list matches any next state. A null current state matches all current states
 *
 * @param currentState the current state of a system
 * @param nextStates the potential next states of a system
 * @param <T> the enum type
 */
public record Transition<T extends Enum<T>>(T currentState, List<T> nextStates) {
    /**
     * Creates a new transition with {@code currentState} and a copy of {@code nextStates}
     *
     * @param currentState the current state of a system
     * @param nextStates the potential next states of a system
     */
    public Transition {
        nextStates = List.copyOf(nextStates);
    }

    /**
     * Creates a new transition with one initial state and one next state
     * Same as {@link #Transition(Enum, List)}
     */
    public Transition(T fromState, T nextState) {
        this(fromState, List.of(nextState));
    }

    /**
     * Returns whether the current state and next state match this transition using the rules specified in {@link Transition}
     *
     * @param otherCurrentState the other current state to match
     * @param otherNextState the other next state to match
     *
     * @return whether this transition matches or not
     */
    public boolean matches(T otherCurrentState, T otherNextState) {
        if (this.currentState == otherCurrentState && this.nextStates.contains(otherNextState)) {
            return true;
        }
        if (this.currentState == null && this.nextStates.contains(otherNextState)) {
            return true;
        }
        if (this.currentState == otherCurrentState && this.nextStates.isEmpty()) {
            return true;
        }
        return this.currentState == null && this.nextStates.isEmpty();
    }

    /**
     * Factory to create a transition that matches the current state and any next state
     *
     * @param currentState the current state of the system
     * @param <J> the type of the transition
     *
     * @return the created transition
     */
    public static <J extends Enum<J>> Transition<J> createFromTransition(J currentState) {
        return new Transition<>(currentState, List.of());
    }

    /**
     * Factory to create a transition that matches the next state and any current state
     *
     * @param nextState the potential next state of the system
     * @param <J> the type of the transition
     *
     * @return the created transition
     */
    public static <J extends Enum<J>> Transition<J> createToTransition(J nextState) {
        return new Transition<>(null, nextState);
    }

    /**
     * Factory to create a transition that matches the current and next state
     *
     * @param currentState the current state of the system
     * @param nextState the potential next state of the system
     * @param <J> the type of the transition
     *
     * @return the created transition
     */
    public static <J extends Enum<J>> Transition<J> createTransition(J currentState, J nextState) {
        return new Transition<>(currentState, nextState);
    }

    /**
     * Factory to create a transition that matches the current and next states
     *
     * @param currentState the current state of the system
     * @param nextState the potential next states of the system
     * @param <J> the type of the transition
     *
     * @return the created transition
     */
    public static <J extends Enum<J>> Transition<J> createTransition(J currentState, List<J> nextState) {
        return new Transition<>(currentState, nextState);
    }
}
