package badgerutils.statemachine;

import org.wpilib.command3.Mechanism;

/**
 * Wraps a {@link StateMachine} inside of a Subsystem for convenience
 *
 * @param <T> the enum type of the state machine
 */
public class StatefulSubsystem<T extends Enum<T>> extends Mechanism {

  private final StateMachine<T> stateMachine;

  /**
   * Constructs a new {@link StatefulSubsystem} using the provided {@link StateMachine}
   *
   * @param stateMachine the {@code StateMachine} to wrap this subsystem around
   */
  public StatefulSubsystem(StateMachine<T> stateMachine) {
    this.stateMachine = stateMachine;
  }

  /** Wraps {@link StateMachine#canChangeState(Enum)} */
  public boolean canChangeState(T toState) {
    return stateMachine.canChangeState(toState);
  }

  /** Wraps {@link StateMachine#canChangeState(Enum)} */
  public boolean tryChangeState(T toState) {
    return stateMachine.tryChangeState(toState);
  }

  /** Wraps {@link StateMachine#getCurrentState()} */
  public T getCurrentState() {
    return stateMachine.getCurrentState();
  }
}
