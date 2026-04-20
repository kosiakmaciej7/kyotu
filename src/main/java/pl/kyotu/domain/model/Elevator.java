package pl.kyotu.domain.model;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import pl.kyotu.domain.exception.InvalidFloorException;

public class Elevator {
    private static final double EPSILON = 1e-3;

    private final int id;
    private final int totalFloors;

    private double currentFloor;
    private Direction direction = Direction.IDLE;
    private ElevatorState state = ElevatorState.IDLE;

    private final TreeSet<Integer> carCalls = new TreeSet<>();
    private final Set<HallCall> assignedHallCalls = new HashSet<>();

    private long doorTimerMs = 0;

    public Elevator(int id, int startingFloor, int totalFloors) {
        if (startingFloor < 0 || startingFloor >= totalFloors) {
            throw new InvalidFloorException("startingFloor out of range");
        }
        this.id = id;
        this.totalFloors = totalFloors;
        this.currentFloor = startingFloor;
    }

    public int id() { return id; }
    public double currentFloor() { return currentFloor; }
    public Direction direction() { return direction; }
    public ElevatorState state() { return state; }
    public Set<Integer> carCalls() { return Set.copyOf(carCalls); }
    public Set<HallCall> assignedHallCalls() { return Set.copyOf(assignedHallCalls); }

    public void addCarCall(FloorNumber floor) {
        if (floor.value() >= totalFloors) {
            throw new InvalidFloorException("Car call floor " + floor.value() + " out of range");
        }
        carCalls.add(floor.value());
    }

    public void assignHallCall(HallCall call) { assignedHallCalls.add(call); }

    public int load() { return carCalls.size() + assignedHallCalls.size(); }

    public boolean isIdle() { return state == ElevatorState.IDLE && direction == Direction.IDLE; }

    public void tick(long dtMs, SimulationParams params) {
        long remaining = dtMs;
        int maxIterations = 200;
        while (remaining > 0 && maxIterations-- > 0) {
            long consumed = processOnce(remaining, params);
            if (consumed == 0) break;
            if (consumed > 0) remaining -= consumed;
        }
    }

    private long processOnce(long dtMs, SimulationParams params) {
        switch (state) {
            case IDLE -> {
                return maybeStartMoving(params);
            }
            case MOVING -> {
                return advance(dtMs, params);
            }
            case DOORS_OPENING, DOORS_CLOSING, DOORS_OPEN -> {
                return advanceDoors(dtMs, params);
            }
            default -> { return dtMs; }
        }
    }

    private long maybeStartMoving(SimulationParams params) {
        Integer nextTarget = pickNextTarget();
        if (nextTarget == null) {
            direction = Direction.IDLE;
            return 0;
        }
        direction = nextTarget > currentFloor ? Direction.UP : Direction.DOWN;
        if (Math.abs(nextTarget - currentFloor) < EPSILON) {
            openDoors(params);
        } else {
            state = ElevatorState.MOVING;
        }
        return -1;
    }

    private long advance(long dtMs, SimulationParams params) {
        double step = (double) dtMs / params.floorTravelMs();
        double next = direction == Direction.UP ? currentFloor + step : currentFloor - step;

        Integer target = pickNextTarget();
        if (target == null) {
            currentFloor = next;
            state = ElevatorState.IDLE;
            direction = Direction.IDLE;
            return dtMs;
        }

        boolean reached = (direction == Direction.UP && next >= target - EPSILON)
                || (direction == Direction.DOWN && next <= target + EPSILON);
        if (reached) {
            double distToTarget = Math.abs(target - currentFloor);
            long msToTarget = (long) Math.ceil(distToTarget * params.floorTravelMs());
            if (msToTarget > dtMs) msToTarget = dtMs;
            currentFloor = target;
            openDoors(params);
            return msToTarget;
        } else {
            currentFloor = next;
            return dtMs;
        }
    }

    private long advanceDoors(long dtMs, SimulationParams params) {
        if (doorTimerMs <= 0) {
            transitionDoorState(params);
            return -1;
        }
        if (dtMs < doorTimerMs) {
            doorTimerMs -= dtMs;
            return dtMs;
        }
        long consumed = doorTimerMs;
        doorTimerMs = 0;
        transitionDoorState(params);
        return consumed;
    }

    private void transitionDoorState(SimulationParams params) {
        switch (state) {
            case DOORS_OPENING -> {
                state = ElevatorState.DOORS_OPEN;
                doorTimerMs = params.doorOpenMs();
            }
            case DOORS_OPEN -> {
                state = ElevatorState.DOORS_CLOSING;
                doorTimerMs = params.doorTransitionMs();
            }
            case DOORS_CLOSING -> {
                int floor = (int) Math.round(currentFloor);
                carCalls.remove(floor);
                assignedHallCalls.removeIf(h -> h.floor().value() == floor
                        && h.direction() == direction);
                assignedHallCalls.removeIf(h -> h.floor().value() == floor);
                Integer nextTarget = pickNextTarget();
                if (nextTarget == null && hasAnyCalls()) {
                    direction = direction == Direction.UP ? Direction.DOWN : Direction.UP;
                    nextTarget = pickNextTarget();
                }
                if (nextTarget == null) {
                    state = ElevatorState.IDLE;
                    direction = Direction.IDLE;
                } else if (Math.abs(nextTarget - currentFloor) < EPSILON) {
                    openDoors(params);
                } else {
                    direction = nextTarget > currentFloor ? Direction.UP : Direction.DOWN;
                    state = ElevatorState.MOVING;
                }
                doorTimerMs = 0;
            }
            default -> {
            }
        }
    }

    private void openDoors(SimulationParams params) {
        state = ElevatorState.DOORS_OPENING;
        doorTimerMs = params.doorTransitionMs();
    }

    private Integer pickNextTarget() {
        TreeSet<Integer> allTargets = new TreeSet<>(carCalls);
        for (HallCall h : assignedHallCalls) allTargets.add(h.floor().value());
        if (allTargets.isEmpty()) return null;

        if (direction == Direction.IDLE) {
            Integer best = null;
            double bestDist = Double.MAX_VALUE;
            for (int f : allTargets) {
                double d = Math.abs(f - currentFloor);
                if (d < bestDist || (d == bestDist && best != null && f > best)) {
                    bestDist = d;
                    best = f;
                }
            }
            return best;
        }

        if (direction == Direction.UP) {
            for (int f : allTargets) if (f >= currentFloor - EPSILON) return f;
            return null;
        } else {
            Integer best = null;
            for (int f : allTargets) if (f <= currentFloor + EPSILON) best = f;
            return best;
        }
    }

    private boolean hasAnyCalls() {
        return !carCalls.isEmpty() || !assignedHallCalls.isEmpty();
    }
}
