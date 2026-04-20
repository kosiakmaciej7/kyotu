package pl.kyotu.adapter.in.web.dto;

import java.util.List;
import java.util.Set;

public record SnapshotResponse(
        int floors,
        double speedMultiplier,
        List<ElevatorDto> elevators,
        Set<HallCallDto> pendingHallCalls
) {}
