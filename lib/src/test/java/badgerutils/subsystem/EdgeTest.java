package badgerutils.subsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeTest {
    private StateMachine<RobotState> stateMachine;

    private boolean eStopped, aStopped;
    private RobotState disabledFrom, enabledTo;
    
    @BeforeEach
    void setup(){
        eStopped = false;
        aStopped = false;
        disabledFrom = null;
        enabledTo = null;
        
        Edges<RobotState> edges = new Edges<>();
        edges.addEnteringEdge(RobotState.DISABLED, Set.of(RobotState.AUTONOMOUS, RobotState.TELEOP, RobotState.TEST), (state) -> enabledTo = state.nextState());
        edges.addLeavingAnyEdge(RobotState.E_STOP, state -> eStopped = true);
        edges.addLeavingAnyEdge(RobotState.A_STOP, state -> aStopped = true);
        edges.addLeavingAnyEdge(RobotState.DISABLED, state -> disabledFrom = state.currentState());
        
        stateMachine = new StateMachine<>(RobotState.DISABLED, edges);
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
        stateMachine.forceChangeState(RobotState.AUTONOMOUS);
        
        stateMachine.tryChangeState(RobotState.DISABLED);
        
        assertEquals(RobotState.AUTONOMOUS, disabledFrom);
    }

    @Test
    void enabledToEdge(){
        assertEquals(RobotState.DISABLED, stateMachine.getCurrentState());

        stateMachine.tryChangeState(RobotState.TEST);
        assertEquals(RobotState.TEST, enabledTo);
    }
}
