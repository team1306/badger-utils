package badgerutils.subsystem;

import java.util.List;

public record Transition<T extends Enum<T>>(T currentState, List<T> nextState) {
    public Transition {
        nextState = List.copyOf(nextState);
    }
    
    public Transition(T fromState, T toState) {
        this(fromState, List.of(toState));
    }
    
    public static <J extends Enum<J>> Transition<J> createFromTransition(J currentState){
        return new Transition<>(currentState, List.of());
    }

    public static <J extends Enum<J>> Transition<J> createToTransition(J nextState){
        return new Transition<>(null, nextState);
    }
    
    public static <J extends Enum<J>> Transition<J> createTransition(J currentState, J nextState){
        return new Transition<>(currentState, nextState);
    }

    public static <J extends Enum<J>> Transition<J> createTransition(J currentState, List<J> nextState){
        return new Transition<>(currentState, nextState);
    }
    
    public boolean matches(T otherCurrentState, T otherNextState) {
        if(this.currentState == otherCurrentState && this.nextState.contains(otherNextState)) {
            return true;
        }
        if(this.currentState == null && this.nextState.contains(otherNextState)) {
            return true;
        }
        if(this.currentState == otherCurrentState && this.nextState.isEmpty()){
            return true;
        }
        if(this.currentState == null && this.nextState.isEmpty()){
            return true;
        }
        
        return false;
    }
}
