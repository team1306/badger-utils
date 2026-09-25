package badgerutils.controls;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControllerMappingTest {
  @BeforeEach
  @AfterEach
  void clearSharedLoop() {
    ControllerMapping.CONTROLS_EVENT_LOOP.clear();
  }

  @Test
  void clearRemovesAllControlBindings() {
    AtomicInteger polls = new AtomicInteger();
    ControllerMapping.CONTROLS_EVENT_LOOP.bind(polls::incrementAndGet);
    TestControllerMapping mapping = new TestControllerMapping();

    ControllerMapping.CONTROLS_EVENT_LOOP.poll();
    mapping.clear();
    ControllerMapping.CONTROLS_EVENT_LOOP.poll();

    assertEquals(1, polls.get());
  }

  private static final class TestControllerMapping extends ControllerMapping {
    TestControllerMapping() {
      super(null, null);
    }

    @Override
    public void bind() {}
  }
}
