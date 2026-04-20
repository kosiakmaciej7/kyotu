package pl.kyotu.domain.service;

import java.util.*;
import org.junit.jupiter.api.Test;
import pl.kyotu.domain.model.*;
import static org.assertj.core.api.Assertions.assertThat;

class CostBasedDispatcherTest {

    private static final int FLOORS = 10;

    private Elevator idle(int id, int floor) {
        return new Elevator(id, floor, FLOORS);
    }

    private HallCall hall(int floor, Direction dir) {
        return new HallCall(new FloorNumber(floor), dir);
    }

    @Test
    void idleNearestWins() {
        Elevator far = idle(0, 0);
        Elevator near = idle(1, 5);
        Set<HallCall> pending = new HashSet<>();
        pending.add(hall(6, Direction.UP));
        new CostBasedDispatcher().assign(List.of(far, near), pending);
        assertThat(near.assignedHallCalls()).hasSize(1);
        assertThat(far.assignedHallCalls()).isEmpty();
        assertThat(pending).isEmpty();
    }

    @Test
    void onTheWayBeatsCloserReverseDirection() {
        Elevator onWay = idle(0, 2);
        onWay.addCarCall(new FloorNumber(9));
        onWay.tick(100, SimulationParams.defaults());

        Elevator closerIdle = idle(1, 4);

        Set<HallCall> pending = new HashSet<>();
        pending.add(hall(5, Direction.UP));
        new CostBasedDispatcher().assign(List.of(onWay, closerIdle), pending);

        assertThat(onWay.assignedHallCalls()).hasSize(1);
    }

    @Test
    void loadedElevatorPenalizedVsIdle() {
        Elevator loaded = idle(0, 5);
        for (int f : new int[]{1, 2, 3, 7, 8, 9}) loaded.addCarCall(new FloorNumber(f));
        Elevator idleSame = idle(1, 5);

        Set<HallCall> pending = new HashSet<>();
        pending.add(hall(5, Direction.UP));
        new CostBasedDispatcher().assign(List.of(loaded, idleSame), pending);

        assertThat(idleSame.assignedHallCalls()).hasSize(1);
    }

    @Test
    void allCallsAssignedAndRemovedFromPending() {
        Elevator e = idle(0, 0);
        Set<HallCall> pending = new HashSet<>(List.of(
                hall(3, Direction.UP),
                hall(5, Direction.UP),
                hall(7, Direction.DOWN)
        ));
        new CostBasedDispatcher().assign(List.of(e), pending);
        assertThat(pending).isEmpty();
        assertThat(e.assignedHallCalls()).hasSize(3);
    }
}
