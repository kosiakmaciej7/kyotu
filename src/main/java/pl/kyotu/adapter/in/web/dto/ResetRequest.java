package pl.kyotu.adapter.in.web.dto;

import jakarta.validation.constraints.Min;

public record ResetRequest(@Min(2) int floors, @Min(1) int cabins) {}
