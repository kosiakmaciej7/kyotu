package pl.kyotu.adapter.out.sse;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.kyotu.adapter.in.web.dto.SnapshotResponse;
import pl.kyotu.adapter.in.web.mapper.SnapshotMapper;
import pl.kyotu.domain.model.Snapshot;
import pl.kyotu.domain.port.out.StateBroadcastPort;

@Component
public class SseBroadcastAdapter implements StateBroadcastPort {

    private static final long EMITTER_TIMEOUT = 0L;
    private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();

    public SseEmitter register() {
        SseEmitter emitter = new SseEmitter(EMITTER_TIMEOUT);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(ex -> emitters.remove(emitter));
        return emitter;
    }

    @Override
    public void broadcast(Snapshot snapshot) {
        SnapshotResponse dto = SnapshotMapper.toResponse(snapshot);
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("state")
                        .reconnectTime(3000)
                        .data(dto));
            } catch (IOException | IllegalStateException ex) {
                emitter.completeWithError(ex);
                emitters.remove(emitter);
            }
        }
    }
}
