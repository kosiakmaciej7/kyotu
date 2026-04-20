package pl.kyotu.domain.model;

import java.util.Set;

public record ElevatorSnapshot(
        int id,
        double floor,
        Direction direction,
        ElevatorState state,
        Set<Integer> carCalls,
        Set<HallCall> assignedHallCalls
) {}
