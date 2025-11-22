package badgerutils.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains a collection of all the specified {@link StateEdge}.
 * <p>
 *     Edges are functions that are executed when the system transitions to another state. 
 *     They may do any operation other than modifying the system's state.
 * <p>
 *     Similar to {@code Guards}, there are 7 types of edges, each slightly different, with very different use cases. 
 *     <ul>
 *         <li>{@link #anyToAny(StateEdge) State to state}</li>
 *         <li>{@link #multipleStatesToMultipleStates(Set, Set, StateEdge) States to states}</li>
 *         <li>{@link #stateToAny(Enum, StateEdge) State to any}</li>
 *         <li>{@link #anyToState(Enum, StateEdge) Any to state}</li>
 *         <li>{@link #anyToAny(StateEdge) Any to any}</li>
 *         <li>{@link #stateToMultipleStates(Enum, Set, StateEdge) State to states}</li>
 *         <li>{@link #multipleStatesToState(Set, Enum, StateEdge) States to state}</li>
 *     </ul>
 * @param <T> the enum type
 */
public class Edges<T extends Enum<T>> {
    private final Map<Transition<T>, List<StateEdge<T>>> edges = new HashMap<>();

    /**
     * Creates a new empty {@link Edges}
     * @return a new {@code Edges}
     * @param <I> the type needed for the {@code Edges}
     */
    public static <I extends Enum<I>> Edges<I> empty() {
        return new Edges<>();
    }

    /**
     * Adds a 'state to state' transition: from one state to one other state
     * @param previousState the previous state of the system
     * @param nextState the next state of the system
     * @param edge the function to execute when the specified transition occurs
     * @return reference for method chaining
     */
    public Edges<T> stateToState(T previousState, T nextState, StateEdge<T> edge) {
        addAllPartialTransitions(Set.of(previousState), Set.of(nextState), edge);
        return this;
    }

    /**
     * Adds a 'state to state' transition for every possible pairing of states: from some states to some other states
     * @param previousStates the possible previous states of the system
     * @param nextStates the possible next states of the system
     * @param edge the function to execute when the specified transition occurs
     * @return reference for method chaining
     */
    public Edges<T> multipleStatesToMultipleStates(Set<T> previousStates, Set<T> nextStates, StateEdge<T> edge){
        addAllPartialTransitions(previousStates, nextStates, edge);
        return this;
    }

    /**
     * Adds a 'state to any state' transition: from one state to any other state
     * @param previousState the previous state of the system
     * @param edge the function to execute when the specified transition occurs
     * @return reference for method chaining
     */
    public Edges<T> stateToAny(T previousState, StateEdge<T> edge){
        addAllPartialTransitions(Set.of(previousState), Set.of(), edge);
        return this;
    }

    /**
     * Adds a 'any state to state' transition: from any state to one other state
     * @param nextState the next state of the system
     * @param edge the function to execute when the specified transition occurs
     * @return reference for method chaining
     */
    public Edges<T> anyToState(T nextState, StateEdge<T> edge){
        addAllPartialTransitions(Set.of(), Set.of(nextState), edge);
        return this;
    }

    /**
     * Adds a 'state to states' transition: from one state to one of the other states
     * @param previousState the previous state of the system
     * @param nextStates the possible next states of the system
     * @param edge the function to execute when the specified transition occurs
     * @return reference for method chaining
     */
    public Edges<T> stateToMultipleStates(T previousState, Set<T> nextStates, StateEdge<T> edge){
        addAllPartialTransitions(Set.of(previousState), nextStates, edge);
        return this;
    }

    /**
     * Adds a 'states to state' transition: from one of the states to one other state
     * @param previousStates the possible previous states of the system
     * @param nextState the next state of the system
     * @param edge the function to execute when the specified transition occurs
     * @return reference for method chaining
     */
    public Edges<T> multipleStatesToState(Set<T> previousStates, T nextState, StateEdge<T> edge){
        addAllPartialTransitions(previousStates, Set.of(nextState), edge);
        return this;
    }

    /**
     * Adds a 'any to any' transition: from any state to any other state
     * @param edge the function to execute when the specified transition occurs
     * @return reference for method chaining
     */
    public Edges<T> anyToAny(StateEdge<T> edge){    
        addAllPartialTransitions(Set.of(), Set.of(), edge);
        return this;
    }

    /**
     * Gets all matching edges for a specified transition
     * @param transition the transition to use for matching edges
     * @return the list of matching edges
     */
    public List<StateEdge<T>> getEdges(Transition<T> transition) {
        ArrayList<StateEdge<T>> edges = new ArrayList<>();
        edges.addAll(getEdgeFromKey(transition));
        edges.addAll(getEdgeFromKey(new Transition<>(transition.previousState(), null)));
        edges.addAll(getEdgeFromKey(new Transition<>(null, transition.nextState())));
        edges.addAll(getEdgeFromKey(new Transition<>(null, null)));
        return edges;
    }
    
    private List<StateEdge<T>> getEdgeFromKey(Transition<T> transition) {
        return edges.getOrDefault(transition, new ArrayList<>());
    }
    
    private void addAllPartialTransitions(Set<T> previousStates, Set<T> nextStates, StateEdge<T> edge) {
        PartialTransition<T> partialPart = new PartialTransition<>(previousStates, nextStates);
        for (Transition<T> transition : partialPart.expandToTransitions()){
            edges.computeIfAbsent(transition, key -> new ArrayList<>()).add(edge);
        }
    }
}
