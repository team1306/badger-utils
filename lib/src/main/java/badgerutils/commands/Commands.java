package badgerutils.commands;

import java.util.function.BooleanSupplier;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.command3.NeedsNameBuilderStage;

public class Commands {

  /**
   * Creates a simple command that runs the given {@link Runnable} once with no subsystem
   * requirements.
   *
   * @param runnable the action to perform when the command executes
   * @return a command builder stage that can be named and scheduled
   */
  public static NeedsNameBuilderStage createInstantCommand(Runnable runnable) {
    return Command.noRequirements(coroutine -> {
      runnable.run();
    });
  }

  /**
   * Creates a command that runs an action once when it starts and another action when it is cancelled.
   * This command will never end naturally and has no requirements.
   *
   * @param onStart the action to perform when the command begins
   * @param onEnd the action to perform when the command is canceled
   * @return a command builder stage that can be named and scheduled
   */
  public static NeedsNameBuilderStage createStartEndCommand(Runnable onStart, Runnable onEnd) {
    return Command.noRequirements(coroutine -> {
      onStart.run();
      coroutine.park();
    }).whenCanceled(onEnd);
  }

  /**
   * Creates a command that runs an action once when it starts and another action when it is cancelled.
   * This command will never end naturally.
   *
   * @param onStart the action to perform when the command begins
   * @param onEnd the action to perform when the command is canceled
   * @param requirement the mechanism that owns this command
   * @return a command builder stage that can be named and scheduled
   */
  public static NeedsNameBuilderStage createStartEndCommand(Runnable onStart, Runnable onEnd, Mechanism requirement) {
    return requirement.run(coroutine -> {
      onStart.run();
      coroutine.park();
    }).whenCanceled(onEnd);
  }

  /**
   * Creates a command that repeatedly executes the given action until the command is canceled.
   *
   * <p>The returned command has no subsystem requirements. It runs {@code whileExecuting} once
   * per scheduler iteration and continues indefinitely until canceled, at which point
   * {@code onEnd} is executed.
   *
   * @param whileExecuting the action to run repeatedly while the command is active
   * @param onEnd the action to perform when the command is canceled
   * @return a command builder stage that can be named and scheduled
   */
  public static NeedsNameBuilderStage createRunEndCommand(Runnable whileExecuting, Runnable onEnd) {
    return Command.noRequirements(coroutine -> {
      while (true) {
        whileExecuting.run();
        coroutine.yield();
      }
    }).whenCanceled(onEnd);
  }

  /**
   * Creates a command that repeatedly executes the given action until the command is canceled.
   *
   * <p>The returned command runs {@code whileExecuting} once
   * per scheduler iteration and continues indefinitely until canceled, at which point
   * {@code onEnd} is executed.
   *
   * @param whileExecuting the action to run repeatedly while the command is active
   * @param onEnd the action to perform when the command is canceled
   * @param requirement the mechanism that owns this command
   * @return a command builder stage that can be named and scheduled
   */
  public static NeedsNameBuilderStage createRunEndCommand(Runnable whileExecuting, Runnable onEnd, Mechanism requirement) {
    return requirement.run(coroutine -> {
      while (true) {
        whileExecuting.run();
        coroutine.yield();
      }
    }).whenCanceled(onEnd);
  }

  /**
   * Creates a guarded command that waits for all conditions to be true before running the target
   * command, and interrupts it immediately if any condition becomes false. This repeats
   * indefinitely until the outer command is canceled.
   *
   * @param command the command to be guarded
   * @param conditions the guard conditions that must all be true
   * @return a NeedsNameBuilderStage managing the guarded execution
   */
  public static NeedsNameBuilderStage createGuardedCommand(
      Command command, BooleanSupplier... conditions) {
    // Combine all guard conditions into a single supplier
    BooleanSupplier allTrue =
        () -> {
          for (BooleanSupplier condition : conditions) {
            if (!condition.getAsBoolean()) {
              return false;
            }
          }
          return true;
        };

    return Command.noRequirements(
        coroutine -> {
          while (true) {
            // 1. Suspend execution until all guard conditions become true
            coroutine.waitUntil(allTrue);

            // 2. Run the target command concurrently against a watcher that waits
            //    for the condition to become false.
            //    awaitAny() finishes as soon as EITHER command completes,
            //    automatically canceling the other unfinished child command.
            coroutine.awaitAny(
                command,
                Command.noRequirements(watcher -> watcher.waitUntil(() -> !allTrue.getAsBoolean()))
                    .named("GuardedCommandWatcher"));

            // 3. Yield to prevent CPU locking if the loop restarts immediately.
            coroutine.yield();
          }
        });
  }
}
