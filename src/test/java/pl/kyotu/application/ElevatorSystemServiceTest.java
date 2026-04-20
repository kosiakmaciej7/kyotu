package pl.kyotu.application;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.kyotu.domain.model.*;
import pl.kyotu.domain.port.out.StateBroadcastPort;
import pl.kyotu.domain.service.CostBasedDispatcher;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ElevatorSystemServiceTest {

    @Test
    void advanceTicksAndBroadcastsSnapshot() {
        StateBroadcastPort broadcaster = mock(StateBroadcastPort.class);
        ElevatorSystemService svc = new ElevatorSystemService(
                new Building(10, 2, new CostBasedDispatcher(), SimulationParams.defaults()),
                broadcaster);
        svc.advance(150);
        ArgumentCaptor<Snapshot> cap = ArgumentCaptor.forClass(Snapshot.class);
        verify(broadcaster).broadcast(cap.capture());
        assertThat(cap.getValue().elevators()).hasSize(2);
    }

    @Test
    void resetChangesBuildingConfig() {
        StateBroadcastPort broadcaster = mock(StateBroadcastPort.class);
        ElevatorSystemService svc = new ElevatorSystemService(
                new Building(5, 1, new CostBasedDispatcher(), SimulationParams.defaults()),
                broadcaster);
        svc.reset(8, 3);
        assertThat(svc.snapshot().floors()).isEqualTo(8);
        assertThat(svc.snapshot().elevators()).hasSize(3);
    }
}
