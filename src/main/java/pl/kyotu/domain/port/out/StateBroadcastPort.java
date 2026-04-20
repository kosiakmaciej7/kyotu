package pl.kyotu.domain.port.out;

import pl.kyotu.domain.model.Snapshot;

public interface StateBroadcastPort {
    void broadcast(Snapshot snapshot);
}
