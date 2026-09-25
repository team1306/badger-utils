package badgerutils.statemachine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TransitionTest {
  @Test
  void transitionBetweenDifferentStatesIsValid() {
    assertTrue(new Transition<>(TestState.IDLE, TestState.RUNNING).isValid());
  }

  @Test
  void transitionToSameStateIsInvalid() {
    assertFalse(new Transition<>(TestState.IDLE, TestState.IDLE).isValid());
  }
}
