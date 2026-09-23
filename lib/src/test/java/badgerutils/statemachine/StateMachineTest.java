package badgerutils.statemachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class StateMachineTest {
  @Test
  void edgeOnlyConstructorAllowsTransitionsAndRunsEdges() {
    AtomicReference<Transition<TestState>> observed = new AtomicReference<>();
    StateMachine<TestState> machine =
        new StateMachine<>(
            TestState.IDLE,
            new Edges<TestState>().stateToState(TestState.IDLE, TestState.RUNNING, observed::set));

    assertTrue(machine.tryChangeState(TestState.RUNNING));

    assertEquals(TestState.RUNNING, machine.getCurrentState());
    assertEquals(new Transition<>(TestState.IDLE, TestState.RUNNING), observed.get());
  }

  @Test
  void guardOnlyConstructorBlocksRejectedTransition() {
    StateMachine<TestState> machine =
        new StateMachine<>(
            TestState.IDLE,
            new Guards<TestState>()
                .stateToState(TestState.IDLE, TestState.RUNNING, transition -> false));

    assertFalse(machine.canChangeState(TestState.RUNNING));
    assertFalse(machine.tryChangeState(TestState.RUNNING));
    assertEquals(TestState.IDLE, machine.getCurrentState());
  }

  @Test
  void sameStateTransitionIsAlwaysRejectedWithoutEvaluatingGuards() {
    AtomicInteger evaluations = new AtomicInteger();
    StateMachine<TestState> machine =
        new StateMachine<>(
            TestState.IDLE,
            new Guards<TestState>()
                .anyToAny(
                    transition -> {
                      evaluations.incrementAndGet();
                      return true;
                    }));

    assertFalse(machine.canChangeState(TestState.IDLE));
    assertFalse(machine.tryChangeState(TestState.IDLE));
    assertEquals(0, evaluations.get());
  }

  @Test
  void allMatchingGuardsMustPassBeforeEdgesRun() {
    AtomicBoolean allow = new AtomicBoolean(false);
    AtomicInteger edgeRuns = new AtomicInteger();
    StateMachine<TestState> machine =
        new StateMachine<>(
            TestState.IDLE,
            new Edges<TestState>().anyToAny(transition -> edgeRuns.incrementAndGet()),
            new Guards<TestState>()
                .stateToState(TestState.IDLE, TestState.RUNNING, transition -> true)
                .anyToAny(transition -> allow.get()));

    assertFalse(machine.tryChangeState(TestState.RUNNING));
    assertEquals(0, edgeRuns.get());
    assertEquals(TestState.IDLE, machine.getCurrentState());

    allow.set(true);
    assertTrue(machine.tryChangeState(TestState.RUNNING));
    assertEquals(1, edgeRuns.get());
    assertEquals(TestState.RUNNING, machine.getCurrentState());
  }

  @Test
  void edgesRunInRegistrationSpecificityOrderBeforeStateChanges() {
    List<String> calls = new ArrayList<>();
    AtomicReference<StateMachine<TestState>> machineReference = new AtomicReference<>();
    Edges<TestState> edges =
        new Edges<TestState>()
            .stateToState(
                TestState.IDLE,
                TestState.RUNNING,
                transition -> {
                  calls.add("exact");
                  assertEquals(TestState.IDLE, machineReference.get().getCurrentState());
                })
            .anyToAny(transition -> calls.add("global"));
    StateMachine<TestState> machine = new StateMachine<>(TestState.IDLE, edges);
    machineReference.set(machine);

    machine.tryChangeState(TestState.RUNNING);

    assertEquals(List.of("exact", "global"), calls);
  }

  @Test
  void setStateWithoutGuardsOrEdgesBypassesBoth() {
    AtomicInteger calls = new AtomicInteger();
    StateMachine<TestState> machine =
        new StateMachine<>(
            TestState.IDLE,
            new Edges<TestState>().anyToAny(transition -> calls.incrementAndGet()),
            new Guards<TestState>().anyToAny(transition -> false));

    machine.setStateWithoutGuardsOrEdges(TestState.FAULT);

    assertEquals(TestState.FAULT, machine.getCurrentState());
    assertEquals(0, calls.get());
  }
}
