package badgerutils.subsystem;

import lombok.Getter;

import java.util.ArrayList;
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
    @Getter
    private T currentState;
    private final Map<Transition<T>, StateEdge<T>> stateEdges;
    private final Map<Transition<T>, StateBoundary<T>> stateBoundaries;

    public StateMachine(T initialState, Map<Transition<T>, StateEdge<T>> stateEdges, Map<Transition<T>, StateBoundary<T>> stateBoundaries) {
        this.currentState = initialState;
        this.stateEdges = stateEdges;
        this.stateBoundaries = stateBoundaries;
    }

    public StateMachine(T initialState, Map<Transition<T>, StateEdge<T>> stateEdges) {
        this(initialState, stateEdges, Map.of());
    }

    public StateMachine(Map<Transition<T>, StateBoundary<T>> stateBoundaries, T initialState) {
        this(initialState, Map.of(), stateBoundaries);
    }

    public boolean canChangeState(T toState) {
        if(toState == currentState) {
            return false;
        }
        List<Transition<T>> matchingTransitions = findMatchingTransitions(stateBoundaries.keySet(), toState);   
        if(matchingTransitions.isEmpty()) {
            return true;
        }
        
        return matchingTransitions.stream().allMatch(transition -> stateBoundaries.get(transition).canChange(currentState, toState));
    }

    public void tryChangeState(T toState) {
        if (!canChangeState(toState)) {
            return;
        }
        
        for (Transition<T> transition : findMatchingTransitions(stateEdges.keySet(), toState)) {
            StateEdge<T> edge = stateEdges.get(transition);
            edge.performTransition(currentState, toState);
        }        
        
        currentState = toState;
    }
    
    private List<Transition<T>> findMatchingTransitions(Set<Transition<T>> possibleTransitions, T toState){
        return possibleTransitions.stream().filter(transition -> transition.matches(currentState, toState)).toList();
    }
}
