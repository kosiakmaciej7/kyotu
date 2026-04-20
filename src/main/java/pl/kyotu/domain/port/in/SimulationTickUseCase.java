package pl.kyotu.domain.port.in;

public interface SimulationTickUseCase {
    void advance(long wallDtMs);
}
