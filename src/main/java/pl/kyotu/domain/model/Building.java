package pl.kyotu.domain.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Synchronized;
import pl.kyotu.domain.exception.ElevatorNotFoundException;
import pl.kyotu.domain.exception.InvalidFloorException;
import pl.kyotu.domain.service.DispatchStrategy;

public class Building {
    private int floors;
    private final List<Elevator> elevators = new ArrayList<>();
    private final Set<HallCall> pending = new LinkedHashSet<>();
    private final DispatchStrategy dispatch;
    private final SimulationParams params;
    private double speedMultiplier = 1.0;

    public Building(int floors, int cabins, DispatchStrategy dispatch, SimulationParams params) {
        this.dispatch = dispatch;
        this.params = params;
        reset(floors, cabins);
    }

    @Synchronized
    public void reset(int floors, int cabins) {
        if (floors < 2 || cabins < 1) {
            throw new IllegalArgumentException("floors must be >= 2 and cabins >= 1");
        }
        this.floors = floors;
        this.elevators.clear();
        this.pending.clear();
        for (int i = 0; i < cabins; i++) {
            this.elevators.add(new Elevator(i, 0, floors));
        }
    }

    @Synchronized
    public int floors() {
        return floors;
    }

    @Synchronized
    public List<Elevator> elevators() {
        return List.copyOf(elevators);
    }

    @Synchronized
    public Set<HallCall> pendingHallCalls() {
        return Set.copyOf(pending);
    }

    @Synchronized
    public double speedMultiplier() {
        return speedMultiplier;
    }

    @Synchronized
    public void setSpeedMultiplier(double m) {
        if (m <= 0) throw new IllegalArgumentException("speed must be > 0");
        this.speedMultiplier = m;
    }

    @Synchronized
    public void addHallCall(HallCall call) {
        if (call.floor().value() >= floors) {
            throw new InvalidFloorException("Hall call floor out of range");
        }
        pending.add(call);
    }

    @Synchronized
    public void addCarCall(int elevatorId, FloorNumber floor) {
        Elevator e = findElevator(elevatorId);
        e.addCarCall(FloorNumber.of(floor.value(), floors));
    }

    @Synchronized
    public void tick(long wallDtMs) {
        long dt = (long) (wallDtMs * speedMultiplier);
        for (Elevator e : elevators) e.tick(dt, params);
        dispatch.assign(elevators, pending);
    }

    public Elevator findElevator(int id) {
        for (Elevator e : elevators) if (e.id() == id) return e;
        throw new ElevatorNotFoundException("Elevator " + id + " not found");
    }

    @Synchronized
    public Snapshot snapshot() {
        List<ElevatorSnapshot> es = elevators.stream()
                .map(e -> new ElevatorSnapshot(
                        e.id(), e.currentFloor(), e.direction(), e.state(),
                        e.carCalls(), e.assignedHallCalls()))
                .toList();
        return new Snapshot(floors, speedMultiplier, es, Set.copyOf(pending));
    }
}
