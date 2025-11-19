package badgerutils.subsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This should not be created by any implementation of the {@link StateMachine}. It is used internally to match states
 */
public record PartialTransition<T extends Enum<T>>(Set<T> currentStates, Set<T> nextStates) {
    public PartialTransition {
        currentStates = Set.copyOf(currentStates);
        nextStates = Set.copyOf(nextStates);
    }
    
    public List<Transition<T>> expandToTransitions(){
        List<Transition<T>> transitions = new ArrayList<>();
        
        if(currentStates.isEmpty() && nextStates.isEmpty()){
            transitions.add(new Transition<>(null, null));
        }
        else if(currentStates.isEmpty()){
            for(T nextState : nextStates){
                transitions.add(new Transition<>(null, nextState));
            }
        }
        else if(nextStates.isEmpty()){
            for(T currentState : currentStates){
                transitions.add(new Transition<>(currentState, null));
            }
        }else{
            for(T currentState : currentStates){
                for(T nextState : nextStates){
                    transitions.add(new Transition<>(currentState, nextState));
                }
            }
        }
        
        return transitions;
    }
}
