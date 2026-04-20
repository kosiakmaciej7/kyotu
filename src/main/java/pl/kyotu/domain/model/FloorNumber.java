package pl.kyotu.domain.model;

import pl.kyotu.domain.exception.InvalidFloorException;

public record FloorNumber(int value) {
    public FloorNumber {
        if (value < 0) {
            throw new InvalidFloorException("Floor number must be >= 0, got " + value);
        }
    }

    public static FloorNumber of(int value, int totalFloors) {
        if (value < 0 || value >= totalFloors) {
            throw new InvalidFloorException(
                    "Floor " + value + " out of range [0, " + totalFloors + ")");
        }
        return new FloorNumber(value);
    }
}
