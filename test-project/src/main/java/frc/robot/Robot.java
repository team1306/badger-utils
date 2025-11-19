package frc.robot;

import badgerlog.Dashboard;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
    
    public Robot(){
        
    }
    
    @Override
    public void robotPeriodic()
    {
        Dashboard.update();
        CommandScheduler.getInstance().run();
    }
    
    @Override
    public void teleopPeriodic() {}
}
