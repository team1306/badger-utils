package badgerutils.subsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Edges<T extends Enum<T>> {
    private final Map<Transition<T>, List<StateEdge<T>>> edges = new HashMap<>();

    public static <I extends Enum<I>> Edges<I> empty() {
        return new Edges<>();
    }

    public Edges<T> leavingStateAndEnteringState(T previousState, T nextState, StateEdge<T> stateEdge) {
        addAllPartialTransitions(Set.of(previousState), Set.of(nextState), stateEdge);
        return this;
    }

    public Edges<T> leavingStatesAndEnteringStates(Set<T> previousStates, Set<T> nextStates, StateEdge<T> edge){
        addAllPartialTransitions(previousStates, nextStates, edge);
        return this;
    }
    
    public Edges<T> leavingToAnyState(T previousState, StateEdge<T> edge){
        addAllPartialTransitions(Set.of(previousState), Set.of(), edge);
        return this;
    }

    public Edges<T> enteringFromAnyState(T nextState, StateEdge<T> edge){
        addAllPartialTransitions(Set.of(), Set.of(nextState), edge);
        return this;
    }

    public Edges<T> leavingToState(T previousState, Set<T> nextStates, StateEdge<T> edge){
        addAllPartialTransitions(Set.of(previousState), nextStates, edge);
        return this;
    }

    public Edges<T> enteringFromState(Set<T> previousStates, T nextState, StateEdge<T> edge){
        addAllPartialTransitions(previousStates, Set.of(nextState), edge);
        return this;
    }
    
    public Edges<T> leavingAndEnteringAnyState(StateEdge<T> edge){    
        addAllPartialTransitions(Set.of(), Set.of(), edge);
        return this;
    }

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
