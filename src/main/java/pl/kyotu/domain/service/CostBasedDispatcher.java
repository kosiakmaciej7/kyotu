package pl.kyotu.domain.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import pl.kyotu.domain.model.Direction;
import pl.kyotu.domain.model.Elevator;
import pl.kyotu.domain.model.HallCall;

public class CostBasedDispatcher implements DispatchStrategy {

    private static final int DIRECTION_PENALTY = 2;
    private static final int LOAD_PENALTY = 1;
    private static final int TOTAL_FLOORS_ASSUMED = 64;

    @Override
    public void assign(List<Elevator> elevators, Set<HallCall> pending) {
        if (elevators.isEmpty() || pending.isEmpty()) return;
        Iterator<HallCall> it = pending.iterator();
        while (it.hasNext()) {
            HallCall call = it.next();
            Elevator best = pickBest(elevators, call);
            if (best != null) {
                best.assignHallCall(call);
                it.remove();
            }
        }
    }

    private Elevator pickBest(List<Elevator> elevators, HallCall call) {
        Elevator best = null;
        double bestCost = Double.MAX_VALUE;
        for (Elevator e : elevators) {
            double cost = cost(e, call);
            if (cost < bestCost) {
                bestCost = cost;
                best = e;
            }
        }
        return best;
    }

    private double cost(Elevator e, HallCall call) {
        double here = e.currentFloor();
        int target = call.floor().value();
        double distance = Math.abs(here - target);

        boolean onTheWay = false;
        double movement;
        if (e.isIdle()) {
            movement = distance;
        } else if (e.direction() == Direction.UP) {
            boolean callAbove = target >= here;
            if (callAbove && call.direction() == Direction.UP) {
                movement = 0;
                onTheWay = true;
            } else {
                movement = distance + DIRECTION_PENALTY * TOTAL_FLOORS_ASSUMED;
            }
        } else {
            boolean callBelow = target <= here;
            if (callBelow && call.direction() == Direction.DOWN) {
                movement = 0;
                onTheWay = true;
            } else {
                movement = distance + DIRECTION_PENALTY * TOTAL_FLOORS_ASSUMED;
            }
        }

        double loadFactor = onTheWay ? 0.4 : LOAD_PENALTY;
        return movement + loadFactor * e.load();
    }
}
