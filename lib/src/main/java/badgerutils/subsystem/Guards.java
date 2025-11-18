package badgerutils.subsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Guards <T extends Enum<T>> {
    private final Map<Transition<T>, List<StateGuardCondition<T>>> guards = new HashMap<>();
    private final Map<PartialTransition<T>, List<StateGuardCondition<T>>> partialGuards = new HashMap<>();

    public static <I extends Enum<I>> Guards<I> empty() {
        return new Guards<>();
    }

    public Guards<T> addTransitionGuard(T currentState, T nextState, StateGuardCondition<T> stateGuard) {
        guards.computeIfAbsent(new Transition<>(currentState, nextState), key -> new ArrayList<>()).add(stateGuard);
        return this;
    }

    public List<StateGuardCondition<T>> getGuards(Transition<T> transition) {
        List<StateGuardCondition<T>> validGuards = new ArrayList<>(guards.getOrDefault(transition, new ArrayList<>()));
        validGuards.addAll(findPartialGuards(transition));
        return validGuards;
    }

    public Guards <T> addTowardsAllGuard(T currentState, StateGuardCondition<T> guard){
        partialGuards.computeIfAbsent(new PartialTransition<>(Set.of(currentState), Set.of()), key -> new ArrayList<>()).add(guard);
        return this;
    }

    public Guards <T> addFromAllGuard(T nextState, StateGuardCondition<T> guard){
        partialGuards.computeIfAbsent(new PartialTransition<>(Set.of(), Set.of(nextState)), key -> new ArrayList<>()).add(guard);
        return this;
    }

    public Guards <T> addTowardsGuard(T currentState, Set<T> nextStates, StateGuardCondition<T> guard){
        partialGuards.computeIfAbsent(new PartialTransition<>(Set.of(currentState), nextStates), key -> new ArrayList<>()).add(guard);
        return this;
    }

    public Guards <T> addFromGuard(Set<T> currentState, T nextStates, StateGuardCondition<T> guard){
        partialGuards.computeIfAbsent(new PartialTransition<>(currentState, Set.of(nextStates)), key -> new ArrayList<>()).add(guard);
        return this;
    }
    
    @SuppressWarnings("Convert2Diamond")
    public Guards <T> addTowardsAndFromEverywhereGuard(StateGuardCondition<T> guard){
        partialGuards.computeIfAbsent(new PartialTransition<T>(Set.of(), Set.of()), key -> new ArrayList<>()).add(guard);
        return this;
    }

    public Guards<T> addTowardsAndFromGuard(Set<T> currentState, Set<T> nextStates, StateGuardCondition<T> guard){
        partialGuards.computeIfAbsent(new PartialTransition<>(currentState, nextStates), key -> new ArrayList<>()).add(guard);
        return this;
    }

    private List<StateGuardCondition<T>> findPartialGuards(Transition<T> transition) {
        return partialGuards.entrySet().stream()
                .filter((kvp) -> kvp.getKey().matches(transition.currentState(), transition.nextState()))
                .collect(ArrayList::new, (list, value) -> list.addAll(value.getValue()), List::addAll);
    }
}
