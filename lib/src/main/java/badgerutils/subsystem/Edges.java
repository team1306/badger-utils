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

    public Edges<T> addEnteringAndLeavingEdge(T currentState, T nextState, StateEdge<T> stateEdge) {
        addAllPartialTransitions(Set.of(currentState), Set.of(nextState), stateEdge);
        return this;
    }

    public Edges<T> addEnteringAndLeavingEdge(Set<T> currentState, Set<T> nextStates, StateEdge<T> edge){
        addAllPartialTransitions(currentState, nextStates, edge);
        return this;
    }
    
    public Edges<T> addEnteringAnyEdge(T currentState, StateEdge<T> edge){
        addAllPartialTransitions(Set.of(currentState), Set.of(), edge);
        return this;
    }

    public Edges<T> addLeavingAnyEdge(T nextState, StateEdge<T> edge){
        addAllPartialTransitions(Set.of(), Set.of(nextState), edge);
        return this;
    }

    public Edges<T> addEnteringEdge(T currentState, Set<T> nextStates, StateEdge<T> edge){
        addAllPartialTransitions(Set.of(currentState), nextStates, edge);
        return this;
    }

    public Edges<T> addLeavingEdge(Set<T> currentState, T nextStates, StateEdge<T> edge){
        addAllPartialTransitions(currentState, Set.of(nextStates), edge);
        return this;
    }
    
    public Edges<T> addEnteringAndLeavingAnyEdge(StateEdge<T> edge){    
        addAllPartialTransitions(Set.of(), Set.of(), edge);
        return this;
    }

    public List<StateEdge<T>> getEdges(Transition<T> transition) {
        ArrayList<StateEdge<T>> edges = new ArrayList<>();
        edges.addAll(getEdgeFromKey(transition));
        edges.addAll(getEdgeFromKey(new Transition<>(transition.currentState(), null)));
        edges.addAll(getEdgeFromKey(new Transition<>(null, transition.nextState())));
        edges.addAll(getEdgeFromKey(new Transition<>(null, null)));
        return edges;
    }
    
    private List<StateEdge<T>> getEdgeFromKey(Transition<T> transition) {
        return edges.getOrDefault(transition, new ArrayList<>());
    }
    
    private void addAllPartialTransitions(Set<T> currentStates, Set<T> nextStates, StateEdge<T> edge) {
        PartialTransition<T> partialPart = new PartialTransition<>(currentStates, nextStates);
        for (Transition<T> transition : partialPart.expandToTransitions()){
            edges.computeIfAbsent(transition, key -> new ArrayList<>()).add(edge);
        }
    }
}
