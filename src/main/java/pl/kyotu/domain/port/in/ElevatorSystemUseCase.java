package pl.kyotu.domain.port.in;

import pl.kyotu.domain.model.Direction;
import pl.kyotu.domain.model.FloorNumber;
import pl.kyotu.domain.model.Snapshot;

public interface ElevatorSystemUseCase {
    Snapshot snapshot();
    void reset(int floors, int cabins);
    void hallCall(FloorNumber floor, Direction direction);
    void carCall(int elevatorId, FloorNumber floor);
    void setSpeed(double multiplier);
}
