package pl.kyotu.domain.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HallCallTest {
    @Test
    void rejectsIdleDirection() {
        assertThatThrownBy(() ->
                new HallCall(new FloorNumber(3), Direction.IDLE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void equalsByFloorAndDirection() {
        HallCall a = new HallCall(new FloorNumber(3), Direction.UP);
        HallCall b = new HallCall(new FloorNumber(3), Direction.UP);
        assertThat(a).isEqualTo(b);
    }
}
