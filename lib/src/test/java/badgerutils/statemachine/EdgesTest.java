package badgerutils.statemachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EdgesTest {
  @Test
  void returnsMatchingEdgesFromMostSpecificToMostGeneral() {
    StateEdge<TestState> exact = transition -> {};
    StateEdge<TestState> previousWildcard = transition -> {};
    StateEdge<TestState> nextWildcard = transition -> {};
    StateEdge<TestState> global = transition -> {};
    Edges<TestState> edges =
        new Edges<TestState>()
            .stateToState(TestState.IDLE, TestState.RUNNING, exact)
            .stateToAny(TestState.IDLE, previousWildcard)
            .anyToState(TestState.RUNNING, nextWildcard)
            .anyToAny(global);

    List<StateEdge<TestState>> matches =
        edges.getEdges(new Transition<>(TestState.IDLE, TestState.RUNNING));

    assertIterableEquals(List.of(exact, previousWildcard, nextWildcard, global), matches);
  }

  @Test
  void registrationMethodsReturnSameInstanceForChaining() {
    Edges<TestState> edges = new Edges<>();
    StateEdge<TestState> edge = transition -> {};

    assertSame(edges, edges.stateToState(TestState.IDLE, TestState.RUNNING, edge));
    assertSame(edges, edges.stateToAny(TestState.IDLE, edge));
    assertSame(edges, edges.anyToState(TestState.RUNNING, edge));
    assertSame(edges, edges.anyToAny(edge));
    assertSame(
        edges,
        edges.stateToMultipleStates(
            TestState.IDLE, Set.of(TestState.RUNNING, TestState.STOPPED), edge));
    assertSame(
        edges,
        edges.multipleStatesToState(
            Set.of(TestState.IDLE, TestState.STOPPED), TestState.RUNNING, edge));
    assertSame(
        edges,
        edges.multipleStatesToMultipleStates(
            Set.of(TestState.IDLE), Set.of(TestState.RUNNING), edge));
  }

  @Test
  void multiStateRegistrationsExpandToEveryPair() {
    StateEdge<TestState> edge = transition -> {};
    Edges<TestState> edges =
        new Edges<TestState>()
            .multipleStatesToMultipleStates(
                Set.of(TestState.IDLE, TestState.STOPPED),
                Set.of(TestState.RUNNING, TestState.FAULT),
                edge);

    for (TestState previous : Set.of(TestState.IDLE, TestState.STOPPED)) {
      for (TestState next : Set.of(TestState.RUNNING, TestState.FAULT)) {
        assertEquals(List.of(edge), edges.getEdges(new Transition<>(previous, next)));
      }
    }
    assertTrue(edges.getEdges(new Transition<>(TestState.RUNNING, TestState.IDLE)).isEmpty());
  }

  @Test
  void returnedListCanBeModifiedWithoutChangingRegistration() {
    StateEdge<TestState> edge = transition -> {};
    Edges<TestState> edges =
        new Edges<TestState>().stateToState(TestState.IDLE, TestState.RUNNING, edge);

    edges.getEdges(new Transition<>(TestState.IDLE, TestState.RUNNING)).clear();

    assertEquals(
        List.of(edge), edges.getEdges(new Transition<>(TestState.IDLE, TestState.RUNNING)));
  }

  @Test
  void emptyFactoryContainsNoEdges() {
    assertTrue(
        Edges.<TestState>empty()
            .getEdges(new Transition<>(TestState.IDLE, TestState.RUNNING))
            .isEmpty());
  }
}
