package pl.kyotu.application;

import lombok.RequiredArgsConstructor;
import pl.kyotu.domain.model.Building;
import pl.kyotu.domain.model.Direction;
import pl.kyotu.domain.model.FloorNumber;
import pl.kyotu.domain.model.HallCall;
import pl.kyotu.domain.model.Snapshot;
import pl.kyotu.domain.port.in.ElevatorSystemUseCase;
import pl.kyotu.domain.port.in.SimulationTickUseCase;
import pl.kyotu.domain.port.out.StateBroadcastPort;

@RequiredArgsConstructor
public class ElevatorSystemService implements ElevatorSystemUseCase, SimulationTickUseCase {

    private final Building building;
    private final StateBroadcastPort broadcaster;

    @Override
    public Snapshot snapshot() {
        return building.snapshot();
    }

    @Override
    public void reset(int floors, int cabins) {
        building.reset(floors, cabins);
        broadcaster.broadcast(building.snapshot());
    }

    @Override
    public void hallCall(FloorNumber floor, Direction direction) {
        building.addHallCall(new HallCall(floor, direction));
    }

    @Override
    public void carCall(int elevatorId, FloorNumber floor) {
        building.addCarCall(elevatorId, floor);
    }

    @Override
    public void setSpeed(double multiplier) {
        building.setSpeedMultiplier(multiplier);
    }

    @Override
    public void advance(long wallDtMs) {
        building.tick(wallDtMs);
        broadcaster.broadcast(building.snapshot());
    }
}
