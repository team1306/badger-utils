package badgerutils.statemachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GuardsTest {
  @Test
  void returnsMatchingGuardsFromMostSpecificToMostGeneral() {
    StateGuardCondition<TestState> exact = transition -> true;
    StateGuardCondition<TestState> previousWildcard = transition -> true;
    StateGuardCondition<TestState> nextWildcard = transition -> true;
    StateGuardCondition<TestState> global = transition -> true;
    Guards<TestState> guards =
        new Guards<TestState>()
            .stateToState(TestState.IDLE, TestState.RUNNING, exact)
            .stateToAny(TestState.RUNNING, nextWildcard)
            .anyToState(TestState.IDLE, previousWildcard)
            .anyToAny(global);

    List<StateGuardCondition<TestState>> matches =
        guards.getGuards(new Transition<>(TestState.IDLE, TestState.RUNNING));

    assertIterableEquals(List.of(exact, previousWildcard, nextWildcard, global), matches);
  }

  @Test
  void registrationMethodsReturnSameInstanceForChaining() {
    Guards<TestState> guards = new Guards<>();
    StateGuardCondition<TestState> guard = transition -> true;

    assertSame(guards, guards.stateToState(TestState.IDLE, TestState.RUNNING, guard));
    assertSame(guards, guards.stateToAny(TestState.IDLE, guard));
    assertSame(guards, guards.anyToState(TestState.RUNNING, guard));
    assertSame(guards, guards.anyToAny(guard));
    assertSame(
        guards,
        guards.stateToMultipleStates(
            TestState.IDLE, Set.of(TestState.RUNNING, TestState.STOPPED), guard));
    assertSame(
        guards,
        guards.multipleStatesToState(
            Set.of(TestState.IDLE, TestState.STOPPED), TestState.RUNNING, guard));
    assertSame(
        guards,
        guards.multipleStatesToMultipleStates(
            Set.of(TestState.IDLE), Set.of(TestState.RUNNING), guard));
  }

  @Test
  void multiStateRegistrationsExpandToEveryPair() {
    StateGuardCondition<TestState> guard = transition -> true;
    Guards<TestState> guards =
        new Guards<TestState>()
            .multipleStatesToMultipleStates(
                Set.of(TestState.IDLE, TestState.STOPPED),
                Set.of(TestState.RUNNING, TestState.FAULT),
                guard);

    for (TestState previous : Set.of(TestState.IDLE, TestState.STOPPED)) {
      for (TestState next : Set.of(TestState.RUNNING, TestState.FAULT)) {
        assertEquals(List.of(guard), guards.getGuards(new Transition<>(previous, next)));
      }
    }
    assertTrue(guards.getGuards(new Transition<>(TestState.RUNNING, TestState.IDLE)).isEmpty());
  }

  @Test
  void returnedListCanBeModifiedWithoutChangingRegistration() {
    StateGuardCondition<TestState> guard = transition -> true;
    Guards<TestState> guards =
        new Guards<TestState>().stateToState(TestState.IDLE, TestState.RUNNING, guard);

    guards.getGuards(new Transition<>(TestState.IDLE, TestState.RUNNING)).clear();

    assertEquals(
        List.of(guard), guards.getGuards(new Transition<>(TestState.IDLE, TestState.RUNNING)));
  }

  @Test
  void emptyFactoryContainsNoGuards() {
    assertTrue(
        Guards.<TestState>empty()
            .getGuards(new Transition<>(TestState.IDLE, TestState.RUNNING))
            .isEmpty());
  }
}
