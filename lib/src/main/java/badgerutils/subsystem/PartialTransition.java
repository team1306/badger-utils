package badgerutils.subsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This should not be created by any implementation of the {@link StateMachine}. It is used internally to match states
 */
public record PartialTransition<T extends Enum<T>>(Set<T> previousStates, Set<T> nextStates) {
    public PartialTransition {
        previousStates = Set.copyOf(previousStates);
        nextStates = Set.copyOf(nextStates);
    }

    /**
     * Creates a list of transitions from the possible states of the {@link PartialTransition}
     * @return the created list of transitions
     */
    public List<Transition<T>> expandToTransitions(){
        List<Transition<T>> transitions = new ArrayList<>();
        
        if(previousStates.isEmpty() && nextStates.isEmpty()){
            transitions.add(new Transition<>(null, null));
        }
        else if(previousStates.isEmpty()){
            for(T nextState : nextStates){
                transitions.add(new Transition<>(null, nextState));
            }
        }
        else if(nextStates.isEmpty()){
            for(T previousState : previousStates){
                transitions.add(new Transition<>(previousState, null));
            }
        }else{
            for(T previousState : previousStates){
                for(T nextState : nextStates){
                    transitions.add(new Transition<>(previousState, nextState));
                }
            }
        }
        
        return transitions;
    }
}
