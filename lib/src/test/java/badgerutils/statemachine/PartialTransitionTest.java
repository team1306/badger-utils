package badgerutils.statemachine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PartialTransitionTest {
  @Test
  void expandsSpecificStatesAsCartesianProduct() {
    PartialTransition<TestState> partial =
        new PartialTransition<>(
            Set.of(TestState.IDLE, TestState.STOPPED), Set.of(TestState.RUNNING, TestState.FAULT));

    assertEquals(
        Set.of(
            new Transition<>(TestState.IDLE, TestState.RUNNING),
            new Transition<>(TestState.IDLE, TestState.FAULT),
            new Transition<>(TestState.STOPPED, TestState.RUNNING),
            new Transition<>(TestState.STOPPED, TestState.FAULT)),
        Set.copyOf(partial.expandToTransitions()));
  }

  @Test
  void emptyPreviousStatesCreatesAnyToSpecificTransitions() {
    PartialTransition<TestState> partial =
        new PartialTransition<>(Set.of(), Set.of(TestState.RUNNING, TestState.FAULT));

    assertEquals(
        Set.of(new Transition<>(null, TestState.RUNNING), new Transition<>(null, TestState.FAULT)),
        Set.copyOf(partial.expandToTransitions()));
  }

  @Test
  void emptyNextStatesCreatesSpecificToAnyTransitions() {
    PartialTransition<TestState> partial =
        new PartialTransition<>(Set.of(TestState.IDLE, TestState.STOPPED), Set.of());

    assertEquals(
        Set.of(new Transition<>(TestState.IDLE, null), new Transition<>(TestState.STOPPED, null)),
        Set.copyOf(partial.expandToTransitions()));
  }

  @Test
  void twoEmptySetsCreateGlobalWildcard() {
    assertEquals(
        List.of(new Transition<TestState>(null, null)),
        new PartialTransition<TestState>(Set.of(), Set.of()).expandToTransitions());
  }

  @Test
  void constructorDefensivelyCopiesSets() {
    Set<TestState> previous = new HashSet<>(Set.of(TestState.IDLE));
    Set<TestState> next = new HashSet<>(Set.of(TestState.RUNNING));
    PartialTransition<TestState> partial = new PartialTransition<>(previous, next);

    previous.add(TestState.FAULT);
    next.clear();

    assertEquals(Set.of(TestState.IDLE), partial.previousStates());
    assertEquals(Set.of(TestState.RUNNING), partial.nextStates());
  }
}
