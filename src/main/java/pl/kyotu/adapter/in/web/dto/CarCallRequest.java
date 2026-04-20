package pl.kyotu.adapter.in.web.dto;

import jakarta.validation.constraints.Min;

public record CarCallRequest(@Min(0) int floor) {}
