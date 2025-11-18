package badgerutils.subsystem;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.Map;

public class StatefulSubsystem<T extends Enum<T>> extends SubsystemBase {
    
    private final StateMachine<T> stateMachine;
    
    public StatefulSubsystem(T initialState, Map<Transition<T>, StateEdge<T>> transitions, Map<Transition<T>, StateBoundary<T>> stateBoundaries) {
        stateMachine = new StateMachine<>(initialState, transitions, stateBoundaries);
    }
    
    public boolean canChangeState(T toState){
        return stateMachine.canChangeState(toState);
    }
    
    public void tryChangeState(T toState){
        stateMachine.tryChangeState(toState);
    }
    
    public T getCurrentState(){
        return stateMachine.getCurrentState();
    }
}
