package pl.kyotu.domain.model;

import org.junit.jupiter.api.Test;
import pl.kyotu.domain.service.CostBasedDispatcher;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SnapshotTest {
    @Test
    void isImmutableCopy() {
        Building b = new Building(5, 2, new CostBasedDispatcher(), SimulationParams.defaults());
        Snapshot s = b.snapshot();
        assertThat(s.elevators()).hasSize(2);
        assertThatThrownBy(() -> s.elevators().add(null))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
