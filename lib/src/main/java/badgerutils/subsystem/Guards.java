package badgerutils.subsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Guards <T extends Enum<T>> {
    private final Map<Transition<T>, List<StateGuardCondition<T>>> edges = new HashMap<>();

    public static <I extends Enum<I>> Guards<I> empty() {
        return new Guards<>();
    }

    public Guards<T> addTransitionGuard(Transition<T> transition, StateGuardCondition<T> stateEdge) {
        edges.computeIfAbsent(transition, key -> new ArrayList<>()).add(stateEdge);
        return this;
    }

    public List<StateGuardCondition<T>> getEdges(Transition<T> transition) {
        return edges.get(transition);
    }
}
