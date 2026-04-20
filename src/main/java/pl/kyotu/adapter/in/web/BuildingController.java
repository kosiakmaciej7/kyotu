package pl.kyotu.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kyotu.adapter.in.web.dto.ResetRequest;
import pl.kyotu.adapter.in.web.dto.SnapshotResponse;
import pl.kyotu.adapter.in.web.mapper.SnapshotMapper;
import pl.kyotu.domain.port.in.ElevatorSystemUseCase;

@RestController
@RequestMapping("/api/building")
@RequiredArgsConstructor
public class BuildingController {

    private final ElevatorSystemUseCase useCase;

    @GetMapping
    public SnapshotResponse get() {
        return SnapshotMapper.toResponse(useCase.snapshot());
    }

    @PostMapping
    public SnapshotResponse reset(@Valid @RequestBody ResetRequest req) {
        useCase.reset(req.floors(), req.cabins());
        return SnapshotMapper.toResponse(useCase.snapshot());
    }
}
