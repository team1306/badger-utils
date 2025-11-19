package badgerutils.subsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Guards <T extends Enum<T>> {
    private final Map<Transition<T>, List<StateGuardCondition<T>>> guards = new HashMap<>();

    public static <I extends Enum<I>> Guards<I> empty() {
        return new Guards<>();
    }

    public Guards<T> addEnteringAndLeavingGuard(T currentState, T nextState, StateGuardCondition<T> guard) {
        addAllPartialTransitions(Set.of(currentState), Set.of(nextState), guard);
        return this;
    }

    public Guards<T> addEnteringAndLeavingGuard(Set<T> currentState, Set<T> nextStates, StateGuardCondition<T> guard){
        addAllPartialTransitions(currentState, nextStates, guard);
        return this;
    }

    public Guards <T> addEnteringAnyGuard(T currentState, StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(currentState), Set.of(), guard);
        return this;
    }

    public Guards <T> addLeavingAnyGuard(T nextState, StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(), Set.of(nextState), guard);
        return this;
    }

    public Guards <T> addEnteringGuard(T currentState, Set<T> nextStates, StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(currentState), nextStates, guard);
        return this;
    }

    public Guards <T> addLeavingGuard(Set<T> currentState, T nextStates, StateGuardCondition<T> guard){
        addAllPartialTransitions(currentState, Set.of(nextStates), guard);
        return this;
    }
    
    public Guards <T> addEnteringAndLeavingAnyGuard(StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(), Set.of(), guard);
        return this;
    }

    public List<StateGuardCondition<T>> getGuards(Transition<T> transition) {
        ArrayList<StateGuardCondition<T>> guards = new ArrayList<>();
        guards.addAll(getGuardFromKey(transition));
        guards.addAll(getGuardFromKey(new Transition<>(transition.currentState(), null)));
        guards.addAll(getGuardFromKey(new Transition<>(null, transition.nextState())));
        guards.addAll(getGuardFromKey(new Transition<>(null, null)));
        return guards;   
    }
    
    private List<StateGuardCondition<T>> getGuardFromKey(Transition<T> transition) {
        return guards.getOrDefault(transition, new ArrayList<>());
    }

    private void addAllPartialTransitions(Set<T> currentStates, Set<T> nextStates, StateGuardCondition<T> guard) {
        PartialTransition<T> partialPart = new PartialTransition<>(currentStates, nextStates);
        for (Transition<T> transition : partialPart.expandToTransitions()){
            guards.computeIfAbsent(transition, key -> new ArrayList<>()).add(guard);
        }
    }
}
