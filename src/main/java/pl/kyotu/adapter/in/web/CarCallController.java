package pl.kyotu.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.kyotu.adapter.in.web.dto.CarCallRequest;
import pl.kyotu.domain.model.FloorNumber;
import pl.kyotu.domain.port.in.ElevatorSystemUseCase;

@RestController
@RequestMapping("/api/elevators/{id}/car-calls")
@RequiredArgsConstructor
public class CarCallController {

    private final ElevatorSystemUseCase useCase;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void create(@PathVariable int id, @Valid @RequestBody CarCallRequest req) {
        useCase.carCall(id, new FloorNumber(req.floor()));
    }
}
