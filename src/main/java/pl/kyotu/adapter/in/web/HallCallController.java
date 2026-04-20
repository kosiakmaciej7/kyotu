package pl.kyotu.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.kyotu.adapter.in.web.dto.HallCallRequest;
import pl.kyotu.domain.model.Direction;
import pl.kyotu.domain.model.FloorNumber;
import pl.kyotu.domain.port.in.ElevatorSystemUseCase;

@RestController
@RequestMapping("/api/hall-calls")
@RequiredArgsConstructor
public class HallCallController {

    private final ElevatorSystemUseCase useCase;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void create(@Valid @RequestBody HallCallRequest req) {
        Direction dir = req.direction() == HallCallRequest.Direction.UP
                ? Direction.UP : Direction.DOWN;
        useCase.hallCall(new FloorNumber(req.floor()), dir);
    }
}
