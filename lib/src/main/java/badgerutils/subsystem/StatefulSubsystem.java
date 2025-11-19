package badgerutils.subsystem;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.Map;

/**
 * Wraps a {@link StateMachine} inside of a Subsystem for convenience
 *
 * @param <T> the enum type of the state machine
 */
public class StatefulSubsystem<T extends Enum<T>> extends SubsystemBase {

    private final StateMachine<T> stateMachine;
    
    public StatefulSubsystem(StateMachine<T> stateMachine) {
        this.stateMachine = stateMachine;
    }

    /**
     * Wraps the {@link StateMachine#canChangeState(Enum)}
     */
    public boolean canChangeState(T toState) {
        return stateMachine.canChangeState(toState);
    }

    /**
     * Wraps the {@link StateMachine#canChangeState(Enum)}
     */
    public boolean tryChangeState(T toState) {
        return stateMachine.tryChangeState(toState);
    }

    /**
     * Wraps the {@link StateMachine#getCurrentState()}
     */
    public T getCurrentState() {
        return stateMachine.getCurrentState();
    }
}
