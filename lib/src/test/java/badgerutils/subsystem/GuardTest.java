package badgerutils.subsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuardTest {
    private StateMachine<RobotState> stateMachine;

    @BeforeEach
    void setup(){
        Guards<RobotState> guards = new Guards<>();
        guards.addEnteringAnyGuard(RobotState.E_STOP, (state) -> false);
        guards.addEnteringAndLeavingGuard(RobotState.A_STOP, RobotState.AUTONOMOUS, (state) -> false);

        stateMachine = new StateMachine<>(RobotState.DISABLED, guards);
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
