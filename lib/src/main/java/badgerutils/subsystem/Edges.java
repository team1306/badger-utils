package badgerutils.subsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Edges<T extends Enum<T>> {
    private final Map<Transition<T>, List<StateEdge<T>>> edges = new HashMap<>();
    private final Map<PartialTransition<T>, List<StateEdge<T>>> partialEdges = new HashMap<>();

    public static <I extends Enum<I>> Edges<I> empty() {
        return new Edges<>();
    }

    public Edges<T> addTransitionEdge(T currentState, T nextState, StateEdge<T> stateEdge) {
        edges.computeIfAbsent(new Transition<>(currentState, nextState), key -> new ArrayList<>()).add(stateEdge);
        return this;
    }

    public List<StateEdge<T>> getEdges(Transition<T> transition) {
        List<StateEdge<T>> validEdges = new ArrayList<>(edges.getOrDefault(transition, new ArrayList<>()));
        validEdges.addAll(findPartialEdges(transition));
        return validEdges;
    }
    
    public Edges<T> addTowardsAllEdge(T currentState, StateEdge<T> edge){
        partialEdges.computeIfAbsent(new PartialTransition<>(Set.of(currentState), Set.of()), key -> new ArrayList<>()).add(edge);
        return this;
    }

    public Edges<T> addFromAllEdge(T nextState, StateEdge<T> edge){
        partialEdges.computeIfAbsent(new PartialTransition<>(Set.of(), Set.of(nextState)), key -> new ArrayList<>()).add(edge);
        return this;
    }

    public Edges<T> addTowardsEdge(T currentState, Set<T> nextStates, StateEdge<T> edge){
        partialEdges.computeIfAbsent(new PartialTransition<>(Set.of(currentState), nextStates), key -> new ArrayList<>()).add(edge);
        return this;
    }

    public Edges<T> addFromEdge(Set<T> currentState, T nextStates, StateEdge<T> edge){
        partialEdges.computeIfAbsent(new PartialTransition<>(currentState, Set.of(nextStates)), key -> new ArrayList<>()).add(edge);
        return this;
    }
    
    @SuppressWarnings("Convert2Diamond")
    public Edges<T> addTowardsAndFromEverywhereEdge(StateEdge<T> edge){
        partialEdges.computeIfAbsent(new PartialTransition<T>(Set.of(), Set.of()), key -> new ArrayList<>()).add(edge);
        return this;
    }
    
    public Edges<T> addTowardsAndFromEdge(Set<T> currentState, Set<T> nextStates, StateEdge<T> edge){
        partialEdges.computeIfAbsent(new PartialTransition<>(currentState, nextStates), key -> new ArrayList<>()).add(edge);
        return this;
    }

    private List<StateEdge<T>> findPartialEdges(Transition<T> transition) {
        return partialEdges.entrySet().stream()
                .filter((kvp) -> kvp.getKey().matches(transition.currentState(), transition.nextState()))
                .collect(ArrayList::new, (list, value) -> list.addAll(value.getValue()), List::addAll);
    }
}
