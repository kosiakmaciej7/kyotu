package pl.kyotu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.kyotu.adapter.out.sse.SseBroadcastAdapter;
import pl.kyotu.application.ElevatorSystemService;
import pl.kyotu.domain.model.Building;
import pl.kyotu.domain.model.SimulationParams;
import pl.kyotu.domain.port.in.ElevatorSystemUseCase;
import pl.kyotu.domain.port.in.SimulationTickUseCase;
import pl.kyotu.domain.port.out.StateBroadcastPort;
import pl.kyotu.domain.service.CostBasedDispatcher;
import pl.kyotu.domain.service.DispatchStrategy;

@Configuration
public class ApplicationConfig {

    @Bean
    SimulationParams simulationParams(ElevatorProperties props) {
        return new SimulationParams(
                props.floorTravelMs(),
                props.doorOpenMs(),
                props.doorTransitionMs(),
                props.directionPenaltyMultiplier(),
                props.loadPenalty());
    }

    @Bean
    DispatchStrategy dispatchStrategy() {
        return new CostBasedDispatcher();
    }

    @Bean
    Building building(ElevatorProperties props, DispatchStrategy strategy, SimulationParams params) {
        return new Building(props.defaultFloors(), props.defaultCabins(), strategy, params);
    }

    @Bean
    @Primary
    StateBroadcastPort broadcastPort(SseBroadcastAdapter adapter) { return adapter; }

    @Bean
    @Primary
    ElevatorSystemUseCase elevatorSystemUseCase(Building b, StateBroadcastPort broadcaster) {
        return new ElevatorSystemService(b, broadcaster);
    }

    @Bean
    SimulationTickUseCase simulationTickUseCase(ElevatorSystemUseCase elevatorSystemUseCase) {
        return (SimulationTickUseCase) elevatorSystemUseCase;
    }
}
