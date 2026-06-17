package badgerutils.statemachine;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Wraps a {@link StateMachine} inside of a Subsystem for convenience
 *
 * @param <T> the enum type of the state machine
 */
public class StatefulSubsystem<T extends Enum<T>> extends SubsystemBase {

    private final StateMachine<T> stateMachine;

    /**
     * Constructs a new {@link StatefulSubsystem} using the provided {@link StateMachine}
     * @param stateMachine the {@code StateMachine} to wrap this subsystem around
     */
    public StatefulSubsystem(StateMachine<T> stateMachine) {
        this.stateMachine = stateMachine;
    }

    /**
     * Wraps {@link StateMachine#canChangeState(Enum)}
     */
    public boolean canChangeState(T toState) {
        return stateMachine.canChangeState(toState);
    }

    /**
     * Wraps {@link StateMachine#canChangeState(Enum)}
     */
    public boolean tryChangeState(T toState) {
        return stateMachine.tryChangeState(toState);
    }

    /**
     * Wraps {@link StateMachine#getCurrentState()}
     */
    public T getCurrentState() {
        return stateMachine.getCurrentState();
    }
}
