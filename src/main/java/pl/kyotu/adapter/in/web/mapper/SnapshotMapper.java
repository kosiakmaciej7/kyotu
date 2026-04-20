package pl.kyotu.adapter.in.web.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import pl.kyotu.adapter.in.web.dto.*;
import pl.kyotu.domain.model.*;

public final class SnapshotMapper {
    private SnapshotMapper() {}

    public static SnapshotResponse toResponse(Snapshot s) {
        return new SnapshotResponse(
                s.floors(),
                s.speedMultiplier(),
                s.elevators().stream().map(SnapshotMapper::toDto).toList(),
                toHallDtos(s.pendingHallCalls())
        );
    }

    public static ElevatorDto toDto(ElevatorSnapshot e) {
        return new ElevatorDto(
                e.id(),
                e.floor(),
                e.direction().name(),
                e.state().name(),
                e.carCalls(),
                toHallDtos(e.assignedHallCalls())
        );
    }

    public static Set<HallCallDto> toHallDtos(Set<HallCall> calls) {
        return calls.stream()
                .map(h -> new HallCallDto(h.floor().value(), h.direction().name()))
                .collect(Collectors.toSet());
    }
}
