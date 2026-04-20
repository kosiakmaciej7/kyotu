package pl.kyotu.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kyotu.adapter.in.web.dto.SpeedRequest;
import pl.kyotu.domain.port.in.ElevatorSystemUseCase;

@RestController
@RequestMapping("/api/simulation")
@RequiredArgsConstructor
public class SimulationController {

    private final ElevatorSystemUseCase useCase;

    @PostMapping("/speed")
    public void setSpeed(@Valid @RequestBody SpeedRequest req) {
        useCase.setSpeed(req.multiplier());
    }
}
