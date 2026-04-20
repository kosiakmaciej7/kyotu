package pl.kyotu.domain.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ElevatorTest {

    private Elevator idleAt(int floor, int totalFloors) {
        return new Elevator(0, floor, totalFloors);
    }

    @Test
    void idleElevatorStaysIdleWithNoCalls() {
        Elevator e = idleAt(3, 10);
        e.tick(150, SimulationParams.defaults());
        assertThat(e.currentFloor()).isEqualTo(3.0);
        assertThat(e.state()).isEqualTo(ElevatorState.IDLE);
        assertThat(e.direction()).isEqualTo(Direction.IDLE);
    }

    @Test
    void movesUpToward_CarCallAbove() {
        Elevator e = idleAt(0, 10);
        e.addCarCall(new FloorNumber(5));
        e.tick(500, SimulationParams.defaults());
        assertThat(e.currentFloor()).isEqualTo(0.5);
        assertThat(e.direction()).isEqualTo(Direction.UP);
        assertThat(e.state()).isEqualTo(ElevatorState.MOVING);
    }

    @Test
    void snapsAndOpensDoorsOnArrival() {
        Elevator e = idleAt(0, 10);
        e.addCarCall(new FloorNumber(1));
        e.tick(1000, SimulationParams.defaults());
        assertThat(e.currentFloor()).isEqualTo(1.0);
        assertThat(e.state()).isEqualTo(ElevatorState.DOORS_OPENING);
        e.tick(500, SimulationParams.defaults());
        assertThat(e.state()).isEqualTo(ElevatorState.DOORS_OPEN);
        e.tick(1500, SimulationParams.defaults());
        assertThat(e.state()).isEqualTo(ElevatorState.DOORS_CLOSING);
        e.tick(500, SimulationParams.defaults());
        assertThat(e.state()).isEqualTo(ElevatorState.IDLE);
        assertThat(e.direction()).isEqualTo(Direction.IDLE);
    }

    @Test
    void lookAlgorithm_servesAllInCurrentDirectionBeforeReversing() {
        Elevator e = idleAt(3, 10);
        e.addCarCall(new FloorNumber(5));
        e.addCarCall(new FloorNumber(8));
        e.addCarCall(new FloorNumber(1));

        for (int i = 0; i < 200; i++) {
            e.tick(100, SimulationParams.defaults());
        }
        assertThat(e.currentFloor()).isEqualTo(1.0);
        assertThat(e.carCalls()).isEmpty();
    }

    @Test
    void rejectsCarCallOutOfRange() {
        Elevator e = idleAt(0, 10);
        org.assertj.core.api.Assertions.assertThatThrownBy(
                () -> e.addCarCall(new FloorNumber(10))
        ).isInstanceOf(pl.kyotu.domain.exception.InvalidFloorException.class);
    }
}
