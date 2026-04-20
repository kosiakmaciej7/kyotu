package pl.kyotu.domain.model;

import java.util.Objects;

public record HallCall(FloorNumber floor, Direction direction) {
    public HallCall {
        Objects.requireNonNull(floor, "floor");
        Objects.requireNonNull(direction, "direction");
        if (direction == Direction.IDLE) {
            throw new IllegalArgumentException("HallCall direction must be UP or DOWN");
        }
    }
}
