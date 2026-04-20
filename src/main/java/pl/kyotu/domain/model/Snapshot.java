package pl.kyotu.domain.model;

import java.util.List;
import java.util.Set;

public record Snapshot(
        int floors,
        double speedMultiplier,
        List<ElevatorSnapshot> elevators,
        Set<HallCall> pendingHallCalls
) {}
