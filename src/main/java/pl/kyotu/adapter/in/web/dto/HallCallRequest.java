package pl.kyotu.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record HallCallRequest(
        @Min(0) int floor,
        @NotNull Direction direction
) {
    public enum Direction { UP, DOWN }
}
