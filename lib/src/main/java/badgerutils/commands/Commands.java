package badgerutils.commands;

import java.util.function.BooleanSupplier;
import org.wpilib.command3.Command;
import org.wpilib.command3.NeedsNameBuilderStage;

public class Commands {
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

            // 3. Yield to prevent CPU locking if the loop restarts immediately [cite: 13, 66]
            coroutine.yield();
          }
        });
  }
}
