package pl.kyotu.domain.service;

import java.util.List;
import java.util.Set;
import pl.kyotu.domain.model.Elevator;
import pl.kyotu.domain.model.HallCall;

public interface DispatchStrategy {
    void assign(List<Elevator> elevators, Set<HallCall> pending);
}
