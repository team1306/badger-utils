package frc.robot;

import badgerlog.Dashboard;
import badgerutils.subsystem.StateBoundary;
import badgerutils.subsystem.StateEdge;
import badgerutils.subsystem.StateMachine;
import badgerutils.subsystem.Transition;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import java.util.List;
import java.util.Map;

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
        Map<Transition<RobotState>, StateEdge<RobotState>> edges = Map.of(
                Transition.createTransition(RobotState.DISABLED, List.of(RobotState.AUTONOMOUS, RobotState.TELEOP, RobotState.TEST)), (current, next) -> System.out.println("Enabled to: " + next),
                Transition.createToTransition(RobotState.E_STOP), (current, next) -> System.out.println("E-Stopped"),
                Transition.createToTransition(RobotState.A_STOP), (current, next) -> System.out.println("A-Stopped"),
                Transition.createToTransition(RobotState.DISABLED), (current, next) -> System.out.println("Disabled from: " + current)
        );
        
        Map<Transition<RobotState>, StateBoundary<RobotState>> boundaries = Map.of(
                Transition.createFromTransition(RobotState.E_STOP), (current, next) -> false,
                Transition.createTransition(RobotState.A_STOP, RobotState.AUTONOMOUS), (current, next) -> false
        );
        
        stateMachine = new StateMachine<>(RobotState.DISABLED, edges, boundaries);
                
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
