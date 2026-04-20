package pl.kyotu.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;

public record SpeedRequest(@DecimalMin("0.1") double multiplier) {}
