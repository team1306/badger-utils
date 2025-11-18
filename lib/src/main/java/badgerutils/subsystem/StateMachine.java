package badgerutils.subsystem;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implements the advanced features possible with a state machine.
 * <p>
 * It specifically enables the use of {@code StateEdge} and {@code StateGuardCondition}.
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
    private final Edges<T> stateEdges;
    private final Guards<T> stateGuards;
    
    @Getter
    private T currentState;
    
    public StateMachine(T initialState, Edges<T> stateEdges, Guards<T> stateGuards) {
        this.currentState = initialState;
        this.stateEdges = stateEdges;
        this.stateGuards = stateGuards;
    }
    
    public StateMachine(T initialState, Edges<T> stateEdges) {
        this(initialState, stateEdges, Guards.empty());
    }
    
    public StateMachine(T initialState, Guards<T> stateGuards) {
        this(initialState, Edges.empty(), stateGuards);
    }
    
    public boolean canChangeState(T nextState) {
        Transition<T> transition = new Transition<>(currentState, nextState);
        
        if(!transition.isValid()) return false;
        
        return stateGuards.getGuards(transition).stream().allMatch((guard) -> guard.canChange(transition));
    }
    
    public void tryChangeState(T nextState) {
        if (!canChangeState(nextState)) {
            return;
        }
        Transition<T> transition = new Transition<>(currentState, nextState);
        
        for(StateEdge<T> edge : stateEdges.getEdges(transition)) {
            edge.performTransition(transition);
        }

        currentState = nextState;
    }
}
