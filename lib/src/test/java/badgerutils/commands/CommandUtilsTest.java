package badgerutils.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

class CommandUtilsTest {
  @Test
  void runIfNotNullConsumesAndReturnsValue() {
    AtomicReference<String> consumed = new AtomicReference<>();

    Optional<String> result = CommandUtils.runIfNotNull("value", consumed::set);

    assertTrue(result.isPresent());
    assertEquals("value", result.orElseThrow());
    assertEquals("value", consumed.get());
  }

  @Test
  void runIfNotNullDoesNothingForNull() {
    AtomicReference<String> consumed = new AtomicReference<>("unchanged");

    Optional<String> result = CommandUtils.runIfNotNull(null, consumed::set);

    assertTrue(result.isEmpty());
    assertEquals("unchanged", consumed.get());
  }

  @Test
  void removeAndCancelDefaultCommandHandlesPresentCommand() {
    TrackingCommand command = new TrackingCommand();
    TestSubsystem subsystem = new TestSubsystem(command);

    CommandUtils.removeAndCancelDefaultCommand(subsystem);

    assertTrue(subsystem.removed);
    assertTrue(command.cancelled);
    assertSame(command, subsystem.originalDefault);
  }

  @Test
  void removeAndCancelDefaultCommandDoesNothingWhenAbsent() {
    TestSubsystem subsystem = new TestSubsystem(null);

    CommandUtils.removeAndCancelDefaultCommand(subsystem);

    assertFalse(subsystem.removed);
  }

  private static final class TrackingCommand extends Command {
    private boolean cancelled;

    @Override
    public void cancel() {
      cancelled = true;
    }
  }

  private static final class TestSubsystem implements Subsystem {
    private final Command originalDefault;
    private Command defaultCommand;
    private boolean removed;

    TestSubsystem(Command defaultCommand) {
      this.originalDefault = defaultCommand;
      this.defaultCommand = defaultCommand;
    }

    @Override
    public Command getDefaultCommand() {
      return defaultCommand;
    }

    @Override
    public void removeDefaultCommand() {
      removed = true;
      defaultCommand = null;
    }
  }
}
