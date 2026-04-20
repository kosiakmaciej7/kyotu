package pl.kyotu.adapter.in.web.dto;

import java.util.Set;

public record ElevatorDto(
        int id,
        double floor,
        String direction,
        String state,
        Set<Integer> carCalls,
        Set<HallCallDto> assignedHallCalls
) {}
