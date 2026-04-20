package pl.kyotu.domain.model;

public record SimulationParams(
        long floorTravelMs,
        long doorOpenMs,
        long doorTransitionMs,
        int directionPenaltyMultiplier,
        int loadPenalty
) {
    public static SimulationParams defaults() {
        return new SimulationParams(1000, 1500, 500, 2, 1);
    }
}
