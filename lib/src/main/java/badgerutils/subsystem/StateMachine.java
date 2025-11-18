package badgerutils.subsystem;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implements the advanced features possible with a state machine.
 * <p>
 * It specifically enables the use of {@code StateEdge} and {@code StateBoundary}.
 * <br />
 * <i>Edges</i> are functions that execute when a state changes from one state to another.
 * An example would be modifying the PID gains when transitioning to a state that requires more accurate tracking.
 * <br />
 * <i>Boundaries</i> are functions that control whether a state can transition to another.
 * An example would be disallowing an arm to swing through the chassis if another mechanism was in the way.
 * </p>
 *
 * @param <T> enum type
 */
public class StateMachine<T extends Enum<T>> {
    private final Map<Transition<T>, StateEdge<T>> stateEdges;
    private final Map<Transition<T>, StateBoundary<T>> stateBoundaries;
    /**
     * -- GETTER --
     * Gets the current state of the state machine
     *
     * @return
     */
    @Getter
    private T currentState;

    /**
     * Creates a new state machine given an initial state, boundaries, and edges.
     *
     * @param initialState the initial state of the machine
     * @param stateEdges the actions to run when the state changes
     * @param stateBoundaries the conditions when a state can transition to another
     */
    public StateMachine(T initialState, Map<Transition<T>, StateEdge<T>> stateEdges, Map<Transition<T>, StateBoundary<T>> stateBoundaries) {
        this.currentState = initialState;
        this.stateEdges = stateEdges;
        this.stateBoundaries = stateBoundaries;
    }

    /**
     * Constructor overload for {@link #StateMachine(Enum, Map, Map)}. {@code StateBoundaries} defaults to an empty map
     */
    public StateMachine(T initialState, Map<Transition<T>, StateEdge<T>> stateEdges) {
        this(initialState, stateEdges, Map.of());
    }

    /**
     * Constructor overload for {@link #StateMachine(Enum, Map, Map)}. {@code stateEdges} defaults to an empty map
     */
    public StateMachine(Map<Transition<T>, StateBoundary<T>> stateBoundaries, T initialState) {
        this(initialState, Map.of(), stateBoundaries);
    }

    /**
     * Returns whether this state machine can change to {@code nextStates} with the current state and state boundaries defined in the constructor.
     *
     * @param nextState the state that might be able to be transitioned to
     *
     * @return whether the state can change to {@code nextStates} or not
     */
    public boolean canChangeState(T nextState) {
        if (nextState == currentState) {
            return false;
        }
        List<Transition<T>> matchingTransitions = findMatchingTransitions(stateBoundaries.keySet(), nextState);
        if (matchingTransitions.isEmpty()) {
            return true;
        }

        return matchingTransitions.stream()
                .allMatch(transition -> stateBoundaries.get(transition).canChange(currentState, nextState));
    }

    /**
     * Tries to modify the state of the system. Calls {@link #canChangeState(Enum)} to check if the state is able to transition to {@code nextStates}
     *
     * @param nextState the next state to transition to
     */
    public void tryChangeState(T nextState) {
        if (!canChangeState(nextState)) {
            return;
        }

        for (Transition<T> transition : findMatchingTransitions(stateEdges.keySet(), nextState)) {
            StateEdge<T> edge = stateEdges.get(transition);
            edge.performTransition(currentState, nextState);
        }

        currentState = nextState;
    }

    private List<Transition<T>> findMatchingTransitions(Set<Transition<T>> possibleTransitions, T toState) {
        return possibleTransitions.stream().filter(transition -> transition.matches(currentState, toState)).toList();
    }
}
