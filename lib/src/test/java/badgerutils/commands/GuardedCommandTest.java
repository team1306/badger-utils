package badgerutils.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class GuardedCommandTest {
  @Test
  void copiesWrappedCommandRequirements() {
    Subsystem requirement = new Subsystem() {};
    TrackingCommand wrapped = new TrackingCommand(requirement);

    GuardedCommand guarded = new GuardedCommand(wrapped, () -> true);

    assertTrue(guarded.hasRequirement(requirement));
  }

  @Test
  void initializesAndExecutesWhenAllConditionsAreTrue() {
    TrackingCommand wrapped = new TrackingCommand();
    GuardedCommand guarded = new GuardedCommand(wrapped, () -> true, () -> true);

    guarded.initialize();
    guarded.execute();
    guarded.execute();

    assertEquals(1, wrapped.initializeCount);
    assertEquals(2, wrapped.executeCount);
    assertEquals(0, wrapped.endCount);
  }

  @Test
  void waitsToInitializeUntilConditionsBecomeTrue() {
    AtomicBoolean allowed = new AtomicBoolean(false);
    TrackingCommand wrapped = new TrackingCommand();
    GuardedCommand guarded = new GuardedCommand(wrapped, allowed::get);

    guarded.initialize();
    assertEquals(0, wrapped.initializeCount);

    allowed.set(true);
    guarded.execute();

    assertEquals(1, wrapped.initializeCount);
    assertEquals(1, wrapped.executeCount);
  }

  @Test
  void interruptsAndCanReinitializeWrappedCommand() {
    AtomicBoolean allowed = new AtomicBoolean(true);
    TrackingCommand wrapped = new TrackingCommand();
    GuardedCommand guarded = new GuardedCommand(wrapped, allowed::get);

    guarded.initialize();
    guarded.execute();
    allowed.set(false);
    guarded.execute();

    assertEquals(1, wrapped.endCount);
    assertTrue(wrapped.lastInterrupted);

    allowed.set(true);
    guarded.execute();
    assertEquals(2, wrapped.initializeCount);
    assertEquals(2, wrapped.executeCount);
  }

  @Test
  void forwardsEndAndResetsInitialization() {
    TrackingCommand wrapped = new TrackingCommand();
    GuardedCommand guarded = new GuardedCommand(wrapped, () -> true);

    guarded.initialize();
    guarded.end(false);
    guarded.execute();

    assertEquals(2, wrapped.initializeCount);
    assertEquals(1, wrapped.endCount);
    assertFalse(wrapped.lastInterrupted);
  }

  private static final class TrackingCommand extends Command {
    private int initializeCount;
    private int executeCount;
    private int endCount;
    private boolean lastInterrupted;

    TrackingCommand(Subsystem... requirements) {
      addRequirements(requirements);
    }

    @Override
    public void initialize() {
      initializeCount++;
    }

    @Override
    public void execute() {
      executeCount++;
    }

    @Override
    public void end(boolean interrupted) {
      endCount++;
      lastInterrupted = interrupted;
    }
  }
}
