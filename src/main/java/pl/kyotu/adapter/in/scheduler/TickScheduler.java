package pl.kyotu.adapter.in.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.kyotu.config.ElevatorProperties;
import pl.kyotu.domain.port.in.SimulationTickUseCase;

@Component
@RequiredArgsConstructor
public class TickScheduler {

    private final SimulationTickUseCase useCase;
    private final ElevatorProperties props;

    @Scheduled(fixedRateString = "${elevator.tick-ms}")
    public void tick() {
        useCase.advance(props.tickMs());
    }
}
