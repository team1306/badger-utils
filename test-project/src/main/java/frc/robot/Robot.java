package frc.robot;

import badgerlog.Dashboard;
import badgerutils.subsystem.Edges;
import badgerutils.subsystem.Guards;
import badgerutils.subsystem.StateEdge;
import badgerutils.subsystem.StateMachine;
import badgerutils.subsystem.Transition;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Robot extends TimedRobot {
    public enum RobotState {
        E_STOP,
        A_STOP,
        DISABLED,
        TELEOP,
        AUTONOMOUS,
        TEST
    }
    
    private final StateMachine<RobotState> stateMachine;
    
    public Robot(){
        Edges<RobotState> edges = new Edges<>();
        edges.addTowardsEdge(RobotState.DISABLED, Set.of(RobotState.AUTONOMOUS, RobotState.TELEOP, RobotState.TEST), (state) -> System.out.println("Enabled to: " + state.nextState()));
        edges.addFromAllEdge(RobotState.E_STOP, state -> System.out.println("E-Stopped"));
        edges.addFromAllEdge(RobotState.A_STOP, state -> System.out.println("A-Stopped"));
        edges.addFromAllEdge(RobotState.DISABLED, state -> System.out.println("Disabled from: " + state.currentState()));
        
        Guards<RobotState> guards = new Guards<>();
        guards.addTowardsAllGuard(RobotState.E_STOP, (state) -> false);
        guards.addTransitionGuard(RobotState.A_STOP, RobotState.AUTONOMOUS, (state) -> false);
        
        stateMachine = new StateMachine<>(RobotState.DISABLED, edges, guards);
                
        Dashboard.createSelectorFromEnum("Robot State Selector", RobotState.class, RobotState.DISABLED, (value) -> {
            stateMachine.tryChangeState((RobotState) value);
            Dashboard.putValue("Robot State", value.toString());
        });
        
    }
    
    @Override
    public void robotPeriodic()
    {
        Dashboard.putValue("Actual Robot State", stateMachine.getCurrentState().toString());
        Dashboard.update();
        CommandScheduler.getInstance().run();
    }
    
    @Override
    public void teleopPeriodic() {}
}
