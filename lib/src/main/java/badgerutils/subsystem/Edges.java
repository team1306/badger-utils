package badgerutils.subsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Edges<T extends Enum<T>> {
    private final Map<Transition<T>, List<StateEdge<T>>> edges = new HashMap<>();
    
    public static <I extends Enum<I>> Edges<I> empty(){
        return new Edges<>();
    };
    
    public Edges<T> addTransitionEdge(Transition<T> transition, StateEdge<T> stateEdge){
        edges.computeIfAbsent(transition, key -> new ArrayList<>()).add(stateEdge);
        return this;
    }
    
    public List<StateEdge<T>> getEdges(Transition<T> transition){
        return edges.get(transition);
    }
}
