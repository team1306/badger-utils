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

    /**
     * Wraps the {@link StateMachine#StateMachine(Enum, Map, Map)} constructor
     */
    public StatefulSubsystem(T initialState, Map<Transition<T>, StateEdge<T>> transitions, Map<Transition<T>, StateBoundary<T>> stateBoundaries) {
        stateMachine = new StateMachine<>(initialState, transitions, stateBoundaries);
    }

    /**
     * Wraps the {@link StateMachine#StateMachine(Enum, Map)} constructor
     */
    public StatefulSubsystem(T initialState, Map<Transition<T>, StateEdge<T>> transitions) {
        stateMachine = new StateMachine<>(initialState, transitions);
    }

    /**
     * Wraps the {@link StateMachine#StateMachine(Map, Enum)} constructor
     */
    public StatefulSubsystem(Map<Transition<T>, StateBoundary<T>> stateBoundaries, T initialState) {
        stateMachine = new StateMachine<>(stateBoundaries, initialState);
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
    public void tryChangeState(T toState) {
        stateMachine.tryChangeState(toState);
    }

    /**
     * Wraps the {@link StateMachine#getCurrentState()}
     */
    public T getCurrentState() {
        return stateMachine.getCurrentState();
    }
}
