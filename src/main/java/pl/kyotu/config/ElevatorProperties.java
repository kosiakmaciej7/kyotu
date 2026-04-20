package pl.kyotu.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "elevator")
public record ElevatorProperties(
        long tickMs,
        long floorTravelMs,
        long doorOpenMs,
        long doorTransitionMs,
        int directionPenaltyMultiplier,
        int loadPenalty,
        int defaultFloors,
        int defaultCabins
) {}
