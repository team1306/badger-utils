package badgerutils.subsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StateMachineTest {
    private StateMachine<RobotState> stateMachine;

    private boolean eStopped, aStopped;
    private RobotState disabledFrom, enabledTo;
    
    
    @BeforeEach
    void setup(){
        eStopped = false;
        aStopped = false;
        disabledFrom = null;
        enabledTo = null;

        Edges<RobotState> edges = new Edges<RobotState>()
                .leavingToState(RobotState.DISABLED, Set.of(RobotState.AUTONOMOUS, RobotState.TELEOP, RobotState.TEST), (state) -> enabledTo = state.nextState())
                .enteringFromAnyState(RobotState.E_STOP, state -> eStopped = true)
                .enteringFromAnyState(RobotState.A_STOP, state -> aStopped = true)
                .enteringFromAnyState(RobotState.DISABLED, state -> disabledFrom = state.previousState());

        Guards<RobotState> guards = new Guards<RobotState>()
                .leavingToAnyState(RobotState.E_STOP, (state) -> false)
                .leavingStateAndEnteringState(RobotState.A_STOP, RobotState.AUTONOMOUS, (state) -> false);

        stateMachine = new StateMachine<>(RobotState.DISABLED, edges, guards);
    }

    @Test
    void eStoppedEdge(){
        assertEquals(RobotState.DISABLED, stateMachine.getCurrentState());

        stateMachine.tryChangeState(RobotState.E_STOP);
        assertTrue(eStopped);
    }

    @Test
    void aStoppedEdge(){
        assertEquals(RobotState.DISABLED, stateMachine.getCurrentState());

        stateMachine.tryChangeState(RobotState.A_STOP);
        assertTrue(aStopped);
    }

    @Test
    void disabledFromEdge(){
        assertEquals(RobotState.DISABLED, stateMachine.getCurrentState());
        stateMachine.setStateWithoutGuardsOrEdges(RobotState.AUTONOMOUS);

        stateMachine.tryChangeState(RobotState.DISABLED);

        assertEquals(RobotState.AUTONOMOUS, disabledFrom);
    }

    @Test
    void enabledToEdge(){
        assertEquals(RobotState.DISABLED, stateMachine.getCurrentState());

        stateMachine.tryChangeState(RobotState.TEST);
        assertEquals(RobotState.TEST, enabledTo);
    }

    @Test
    void eStopGuardTest(){
        assertEquals(RobotState.DISABLED, stateMachine.getCurrentState());

        assertTrue(stateMachine.tryChangeState(RobotState.E_STOP));
        assertEquals(RobotState.E_STOP, stateMachine.getCurrentState());

        assertFalse(stateMachine.tryChangeState(RobotState.AUTONOMOUS));
        assertFalse(stateMachine.tryChangeState(RobotState.TEST));

        assertEquals(RobotState.E_STOP, stateMachine.getCurrentState());
    }

    @Test
    void aStopGuardTest(){
        assertEquals(RobotState.DISABLED, stateMachine.getCurrentState());

        assertTrue(stateMachine.tryChangeState(RobotState.A_STOP));
        assertEquals(RobotState.A_STOP, stateMachine.getCurrentState());

        assertFalse(stateMachine.tryChangeState(RobotState.AUTONOMOUS));
        assertEquals(RobotState.A_STOP, stateMachine.getCurrentState());

        assertTrue(stateMachine.tryChangeState(RobotState.TEST));
        assertEquals(RobotState.TEST, stateMachine.getCurrentState());
    }

    @Test
    void normalGuardTest(){

    }
}
