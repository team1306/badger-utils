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

    public Guards<T> leavingStateAndEnteringState(T previousState, T nextState, StateGuardCondition<T> guard) {
        addAllPartialTransitions(Set.of(previousState), Set.of(nextState), guard);
        return this;
    }

    public Guards<T> leavingStatesAndEnteringStates(Set<T> previousState, Set<T> nextStates, StateGuardCondition<T> guard){
        addAllPartialTransitions(previousState, nextStates, guard);
        return this;
    }

    public Guards <T> leavingToAnyState(T previousState, StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(previousState), Set.of(), guard);
        return this;
    }

    public Guards <T> enteringFromAnyState(T nextState, StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(), Set.of(nextState), guard);
        return this;
    }

    public Guards <T> leavingToState(T previousState, Set<T> nextStates, StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(previousState), nextStates, guard);
        return this;
    }

    public Guards <T> enteringFromState(Set<T> previousState, T nextStates, StateGuardCondition<T> guard){
        addAllPartialTransitions(previousState, Set.of(nextStates), guard);
        return this;
    }
    
    public Guards <T> leavingAndEnteringAnyState(StateGuardCondition<T> guard){
        addAllPartialTransitions(Set.of(), Set.of(), guard);
        return this;
    }

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
