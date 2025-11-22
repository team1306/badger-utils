package badgerutils.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains a collection of all the specified {@link StateGuardCondition}.
 * <p>
 *     Guards are functions that are checked for true when attempting to transition to another state. 
 *     They may never modify a system's state
 * <p>
 *     Similar to {@code Edges}, there are 7 types of guards, each slightly different, with very different use cases.
 *     <ul>
 *         <li>{@link #leavingAndEnteringAnyState(StateGuardCondition) State to state}</li>
 *         <li>{@link #leavingStatesAndEnteringStates(Set, Set, StateGuardCondition) States to states}</li>
 *         <li>{@link #leavingToAnyState(Enum, StateGuardCondition) State to any}</li>
 *         <li>{@link #enteringFromAnyState(Enum, StateGuardCondition) Any to state}</li>
 *         <li>{@link #leavingAndEnteringAnyState(StateGuardCondition) Any to any}</li>
 *         <li>{@link #leavingToState(Enum, Set, StateGuardCondition) State to states}</li>
 *         <li>{@link #enteringFromState(Set, Enum, StateGuardCondition) States to state}</li>
 *     </ul>
 * @param <T> the enum type
 */
public class Guards <T extends Enum<T>> {
    private final Map<Transition<T>, List<StateGuardCondition<T>>> guards = new HashMap<>();

    /**
     * Creates a new empty {@link Guards}
     * @return a new {@code Guards}
     * @param <I> the type needed for the {@code Guards}
     */
    public static <I extends Enum<I>> Guards<I> empty() {
        return new Guards<>();
    }

    /**
     * Adds a 'state to state' transition: from one state to one other state
     * @param previousState the previous state of the system
     * @param nextState the next state of the system
     * @param guard the function to check before the system transitions state
     * @return reference for method chaining
     */
    public Guards<T> leavingStateAndEnteringState(T previousState, T nextState, StateGuardCondition<T> guard) {
        addAllPartialTransitions(Set.of(previousState), Set.of(nextState), guard);
        return this;
    }

    /**
     * Adds a 'state to state' transition for every possible pairing of states: from some states to some other states
     * @param previousStates the possible previous states of the system
     * @param nextStates the possible next states of the system
     * @param guard the function to check before the system transitions state
     * @return reference for method chaining
     */
    public Guards<T> leavingStatesAndEnteringStates(Set<T> previousStates, Set<T> nextStates, StateGuardCondition<T> guard){
        addAllPartialTransitions(previousStates, nextStates, guard);
        return this;
    }

    /**
     * Adds a 'state to any state' transition: from one state to any other state
     * @param previousState the previous state of the system
     * @param guard the function to check before the system transitions state
     * @return reference for method chaining
     */
    public Guards <T> leavingToAnyState(T previousState, StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(previousState), Set.of(), guard);
        return this;
    }

    /**
     * Adds a 'any state to state' transition: from any state to one other state
     * @param nextState the next state of the system
     * @param guard the function to check before the system transitions state
     * @return reference for method chaining
     */
    public Guards <T> enteringFromAnyState(T nextState, StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(), Set.of(nextState), guard);
        return this;
    }

    /**
     * Adds a 'state to states' transition: from one state to one of the other states
     * @param previousState the previous state of the system
     * @param nextStates the possible next states of the system
     * @param guard the function to check before the system transitions state
     * @return reference for method chaining
     */
    public Guards <T> leavingToState(T previousState, Set<T> nextStates, StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(previousState), nextStates, guard);
        return this;
    }

    /**
     * Adds a 'states to state' transition: from one of the states to one other state
     * @param previousStates the possible previous states of the system
     * @param nextState the next state of the system
     * @param guard the function to check before the system transitions state
     * @return reference for method chaining
     */
    public Guards <T> enteringFromState(Set<T> previousStates, T nextState, StateGuardCondition<T> guard){
        addAllPartialTransitions(previousStates, Set.of(nextState), guard);
        return this;
    }

    /**
     * Adds a 'any to any' transition: from any state to any other state
     * @param guard the function to check before the system transitions state
     * @return reference for method chaining
     */
    public Guards <T> leavingAndEnteringAnyState(StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(), Set.of(), guard);
        return this;
    }

    /**
     * Gets all matching guards for a specified transition
     * @param transition the transition to use for matching guards
     * @return the list of matching guards
     */
    public List<StateGuardCondition<T>> getGuards(Transition<T> transition) {
        ArrayList<StateGuardCondition<T>> guards = new ArrayList<>();
        guards.addAll(getGuardFromKey(transition));
        guards.addAll(getGuardFromKey(new Transition<>(transition.previousState(), null)));
        guards.addAll(getGuardFromKey(new Transition<>(null, transition.nextState())));
        guards.addAll(getGuardFromKey(new Transition<>(null, null)));
        return guards;   
    }
    
    private List<StateGuardCondition<T>> getGuardFromKey(Transition<T> transition) {
        return guards.getOrDefault(transition, new ArrayList<>());
    }

    private void addAllPartialTransitions(Set<T> previousStates, Set<T> nextStates, StateGuardCondition<T> guard) {
        PartialTransition<T> partialPart = new PartialTransition<>(previousStates, nextStates);
        for (Transition<T> transition : partialPart.expandToTransitions()){
            guards.computeIfAbsent(transition, key -> new ArrayList<>()).add(guard);
        }
    }
}
