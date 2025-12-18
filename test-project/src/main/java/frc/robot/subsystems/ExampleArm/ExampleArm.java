package frc.robot.subsystems.ExampleArm;

import badgerlog.annotations.Entry;
import badgerlog.annotations.EntryType;
import badgerlog.annotations.Watched;
import badgerlog.annotations.Watcher;
import badgerlog.events.EventData;
import badgerutils.motor.MotorConfigUtils;
import badgerutils.statemachine.*;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import javax.security.auth.login.Configuration;

import static edu.wpi.first.units.Units.*;

public class ExampleArm extends SubsystemBase {

    private static final double GEAR_RATIO = 1/60; //example gear ratio is 60:1

    @Watched("updatePid")
    @Entry(EntryType.SUBSCRIBER)
    private double kP = 0.1, kI = 0, kD = 0, kS = 0, kV = 0.08, kA = 0, kG = 0.073;

    private TalonFX leftMotor;
    private TalonFX rightMotor;

    @Entry(EntryType.PUBLISHER)
    private Rotation2d targetRotation;

    private StateMachine<ExampleArmStates> stateMachine;

    public ExampleArm(ExampleWrist wrist) {
        //STATE MACHINE
        //When transitioning to each state, set the target rotation to the desired degrees
        Edges<ExampleArmStates> edges = new Edges<ExampleArmStates>()
                .anyToState(ExampleArmStates.ARM_UP, this::toUpState)
                .anyToState(ExampleArmStates.ARM_LEVEL, (state) -> setTargetRotation(Rotation2d.fromDegrees(0)))
                .anyToState(ExampleArmStates.ARM_DOWN, (state) -> setTargetRotation(Rotation2d.fromDegrees(-30)));

        //Only go to the upper position only if the wrist is rotated horizontally
        Guards<ExampleArmStates> guards = new Guards<ExampleArmStates>()
                .anyToState(ExampleArmStates.ARM_UP, (state) -> wrist.getCurrentState() == ExampleWristStates.HORIZONTAL);


        //CONFIGURATION
        //General configurations (PID and profiling)
        TalonFXConfiguration config = new TalonFXConfiguration()
                .withMotionMagic(MotorConfigUtils.createMotionMagicConfig(50, 10))
                .withSlot0(MotorConfigUtils.createPidConfig(kP, kI, kD, kS, kV, kG, kA, GravityTypeValue.Arm_Cosine));

        //Each motor has a different motorOutputConfigs because they are inverted
        leftMotor.getConfigurator().apply(config.withMotorOutput(MotorConfigUtils.createMotorOutputConfig(InvertedValue.Clockwise_Positive, NeutralModeValue.Brake)));
        rightMotor.getConfigurator().apply(config.withMotorOutput(MotorConfigUtils.createMotorOutputConfig(InvertedValue.CounterClockwise_Positive, NeutralModeValue.Brake)));
    }

    private void toUpState(Transition<ExampleArmStates> transition) {
        targetRotation = Rotation2d.fromDegrees(60);
    }

    @Override
    public void periodic() {
        //MotionMagicVoltage does all the PID math automatically. It just needs a position input
        MotionMagicVoltage motorPower = new MotionMagicVoltage(getMotorRotationFromReal(targetRotation).getDegrees());

        leftMotor.setControl(motorPower);
        rightMotor.setControl(motorPower);
    }

    @Watcher(type = double.class, name = "updatePid")
    private void updatePid(EventData<Double> data) {
        Slot0Configs config = MotorConfigUtils.createPidConfig(kP, kI, kD, kS, kV, kG, kA, GravityTypeValue.Arm_Cosine);
        leftMotor.getConfigurator().apply(config);
        rightMotor.getConfigurator().apply(config);
    }

    public boolean setGoalState(ExampleArmStates state) {
        return stateMachine.tryChangeState(state);
    }

    private void setTargetRotation(Rotation2d targetRotation) {
        this.targetRotation = targetRotation;
    }

    private Rotation2d getMotorRotationFromReal(Rotation2d realRotation) {
        return realRotation.div(GEAR_RATIO);
    }
}
