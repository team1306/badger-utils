package badgerutils.statemachine;

import lombok.Getter;

/**
 * Implements the advanced features possible with a state machine.
 * <p>
 * It specifically enables the use of {@code StateEdge} and {@code StateGuardCondition}.
 * <br />
 * <i>Edges</i> are functions that execute when a state changes from one state to another.
 * An example would be modifying the PID gains when transitioning to a state that requires more accurate tracking.
 * <br />
 * <i>Guards</i> are functions that control whether a state can transition to another.
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

    /**
     * Creates a new {@link StateMachine} object with the current state set to {@code initialState} 
     * @param initialState the intial state of the system
     * @param stateEdges the edges of the system
     * @param stateGuards the guards of the system
     * @see Edges
     * @see Guards
     */
    public StateMachine(T initialState, Edges<T> stateEdges, Guards<T> stateGuards) {
        this.currentState = initialState;
        this.stateEdges = stateEdges;
        this.stateGuards = stateGuards;
    }

    /**
     * Similar to {@link #StateMachine(Enum, Edges, Guards)} except that {@code stateGuards} defaults to empty
     */
    public StateMachine(T initialState, Edges<T> stateEdges) {
        this(initialState, stateEdges, Guards.empty());
    }

    /**
     * Similar to {@link #StateMachine(Enum, Edges, Guards)} except that {@code stateEdges} defaults to empty
     */
    public StateMachine(T initialState, Guards<T> stateGuards) {
        this(initialState, Edges.empty(), stateGuards);
    }

    /**
     * Checks whether the current system's state can change to {@code nextState} using the {@link Guards} provided. 
     * <p>The state is not allowed to change to itself</p>
     * @param nextState the potential next state of the system
     * @return if the state can change to {@code nextState}
     */
    public boolean canChangeState(T nextState) {
        Transition<T> transition = new Transition<>(currentState, nextState);
        
        if(!transition.isValid()) return false;
        
        return stateGuards.getGuards(transition).stream().allMatch((guard) -> guard.canChange(transition));
    }

    /**
     * Tries to change the state of the system to {@code nextState}, executing any matching {@link Edges}. 
     * <p>Uses {@link #canChangeState(Enum)} to verify that the state can actually change.</p>
     * @param nextState the potential next state of the system
     * @return whether the state changed or not
     */
    public boolean tryChangeState(T nextState) {
        if (!canChangeState(nextState)) {
            return false;
        }
        Transition<T> transition = new Transition<>(currentState, nextState);
        
        for(StateEdge<T> edge : stateEdges.getEdges(transition)) {
            edge.performTransition(transition);
        }

        currentState = nextState;
        return true;
    }

    /**
     * Changes the state of the system to {@code nextState} without checking the {@link Guards} or running the {@link Edges}.
     * <p>Not recommended for general use</p>
     * @param nextState the next state of the system
     */
    public void setStateWithoutGuardsOrEdges(T nextState) {
        currentState = nextState;
    }
}
