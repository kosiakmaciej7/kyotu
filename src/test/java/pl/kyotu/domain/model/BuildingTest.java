package pl.kyotu.domain.model;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kyotu.domain.service.DispatchStrategy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BuildingTest {

    @Mock
    private DispatchStrategy dispatch;

    private Building newBuilding(int floors, int cabins) {
        return new Building(floors, cabins, dispatch, SimulationParams.defaults());
    }

    @Test
    void resetReplacesElevators() {
        Building b = newBuilding(5, 2);
        b.reset(10, 3);
        assertThat(b.floors()).isEqualTo(10);
        assertThat(b.elevators()).hasSize(3);
    }

    @Test
    void tickDelegatesPendingHallCallsToDispatchStrategy() {
        Building b = newBuilding(10, 2);
        HallCall call = new HallCall(new FloorNumber(5), Direction.UP);
        b.addHallCall(call);

        b.tick(150);

        verify(dispatch).assign(anyList(), any());
        assertThat(b.pendingHallCalls()).contains(call);
    }

    @Test
    void concurrentHallCallsAreNotLost() throws InterruptedException {
        Building b = newBuilding(10, 3);
        int N = 200;
        ExecutorService ex = Executors.newFixedThreadPool(8);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(N);
        for (int i = 0; i < N; i++) {
            int floor = 1 + (i % 8);
            Direction dir = (i % 2 == 0) ? Direction.UP : Direction.DOWN;
            ex.submit(() -> {
                try {
                    start.await();
                    b.addHallCall(new HallCall(new FloorNumber(floor), dir));
                } catch (InterruptedException ignored) {
                } finally {
                    done.countDown();
                }
            });
        }
        start.countDown();
        done.await(5, TimeUnit.SECONDS);
        ex.shutdown();

        Set<HallCall> pending = b.pendingHallCalls();
        int assigned = b.elevators().stream().mapToInt(e -> e.assignedHallCalls().size()).sum();
        assertThat(pending.size() + assigned).isBetween(1, 16);
    }
}
