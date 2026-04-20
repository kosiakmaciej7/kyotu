package pl.kyotu.domain;

import org.junit.jupiter.api.Test;
import pl.kyotu.domain.model.*;
import pl.kyotu.domain.service.CostBasedDispatcher;
import static org.assertj.core.api.Assertions.assertThat;

class SimulationScenarioTest {

    @Test
    void threeElevatorsTenFloorsServesAllCalls() {
        Building b = new Building(10, 3, new CostBasedDispatcher(), SimulationParams.defaults());

        b.addHallCall(new HallCall(new FloorNumber(3), Direction.UP));
        b.addHallCall(new HallCall(new FloorNumber(7), Direction.DOWN));
        b.addHallCall(new HallCall(new FloorNumber(5), Direction.UP));

        boolean carCallAdded = false;
        for (int i = 0; i < 400; i++) {
            b.tick(150);
            if (!carCallAdded && i == 20) {
                b.addCarCall(0, new FloorNumber(9));
                carCallAdded = true;
            }
        }

        assertThat(b.pendingHallCalls()).isEmpty();
        for (Elevator e : b.elevators()) {
            assertThat(e.assignedHallCalls()).as("elevator %d", e.id()).isEmpty();
            assertThat(e.carCalls()).as("elevator %d", e.id()).isEmpty();
        }
    }
}
