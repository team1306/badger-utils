package frc.robot.subsystems.ExampleArm;

import badgerutils.statemachine.*;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ExampleArm extends SubsystemBase {

    private TalonFX leftMotor;
    private TalonFX rightMotor;

    private Rotation2d targetRotation;

    private StateMachine<ExampleArmStates> stateMachine;

    public ExampleArm(ExampleWrist wrist) {
        //When transitioning to each state, set the target rotation to the desired degrees
        Edges<ExampleArmStates> edges = new Edges<ExampleArmStates>()
                .anyToState(ExampleArmStates.ARM_UP, (state) -> setTargetRotation(Rotation2d.fromDegrees(60)))
                .anyToState(ExampleArmStates.ARM_LEVEL, (state) -> setTargetRotation(Rotation2d.fromDegrees(0)))
                .anyToState(ExampleArmStates.ARM_DOWN, (state) -> setTargetRotation(Rotation2d.fromDegrees(-30)));

        //Only go to the upper position if the wrist is rotated less than .1 degrees
        Guards<ExampleArmStates> guards = new Guards<ExampleArmStates>()
                .enteringFromAnyState(ExampleArmStates.ARM_UP, (state) -> Math.abs(wrist.getCurrentRotation().getDegrees()) < 0.1);

    }

    private void setTargetRotation(Rotation2d targetRotation) {
        this.targetRotation = targetRotation;
    }
}
