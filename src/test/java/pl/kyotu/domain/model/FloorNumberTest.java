package pl.kyotu.domain.model;

import org.junit.jupiter.api.Test;
import pl.kyotu.domain.exception.InvalidFloorException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FloorNumberTest {

    @Test
    void acceptsZeroAndPositive() {
        assertThat(new FloorNumber(0).value()).isZero();
        assertThat(new FloorNumber(42).value()).isEqualTo(42);
    }

    @Test
    void rejectsNegative() {
        assertThatThrownBy(() -> new FloorNumber(-1))
                .isInstanceOf(InvalidFloorException.class)
                .hasMessageContaining("-1");
    }

    @Test
    void ofWithBoundsValidatesUpper() {
        assertThat(FloorNumber.of(5, 10).value()).isEqualTo(5);
        assertThatThrownBy(() -> FloorNumber.of(10, 10))
                .isInstanceOf(InvalidFloorException.class);
    }
}
