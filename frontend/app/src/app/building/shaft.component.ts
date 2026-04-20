import { Component, computed, input } from '@angular/core';
import { ElevatorDto } from '../core/models';

const FLOOR_H = 56;

@Component({
  standalone: true,
  selector: 'app-shaft',
  template: `
    <div class="shaft" [style.minHeight.px]="floors() * FLOOR_H">
      <div class="shaft-label">SHAFT {{ labelIndex() }}</div>

      @for (t of targetFloors(); track t) {
        <div class="call-marker" [style.bottom.px]="markerY(t)"></div>
      }

      <div class="cabin"
           [class.moving]="elevator().state === 'MOVING'"
           [class.doors-open]="elevator().state === 'DOORS_OPEN'"
           [style.transform]="'translateY(' + cabinY() + 'px)'">
        <div class="cabin-floor">{{ pad(Math.floor(elevator().floor)) }}</div>
        <div class="cabin-dir">
          @if (elevator().direction === 'UP') {
            <svg width="10" height="10" viewBox="0 0 10 10" fill="currentColor"><path d="M5 1.5 L8.5 7 L1.5 7 Z"/></svg>
          }
          @if (elevator().direction === 'DOWN') {
            <svg width="10" height="10" viewBox="0 0 10 10" fill="currentColor"><path d="M5 8.5 L1.5 3 L8.5 3 Z"/></svg>
          }
        </div>
        <div class="cabin-doors">
          <span class="door left"></span>
          <span class="door right"></span>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .shaft { position: relative; width: var(--shaft-width); flex: 1;
      border-right: 1px solid var(--border-hairline);
      background: repeating-linear-gradient(to bottom, transparent 0,
        transparent calc(var(--floor-height) - 1px),
        var(--border-hairline) calc(var(--floor-height) - 1px),
        var(--border-hairline) var(--floor-height)); }
    .shaft:last-child { border-right: none; }
    .shaft-label { position: absolute; top: 10px; left: 50%;
      transform: translateX(-50%); font-family: var(--font-mono); font-size: 9px;
      font-weight: 600; letter-spacing: 0.16em; text-transform: uppercase;
      color: var(--text-muted); z-index: 1; pointer-events: none; }
    .call-marker { position: absolute; right: 4px; width: 3px; height: 3px;
      background: var(--amber); border-radius: 50%;
      box-shadow: 0 0 4px var(--amber-glow); pointer-events: none; }
    .cabin { position: absolute; left: 10%; right: 10%;
      height: calc(var(--floor-height) - 8px);
      background: var(--surface-3); border: 1px solid var(--border-strong);
      border-radius: var(--r-md);
      transition: transform 180ms linear, border-color 200ms ease;
      display: grid; grid-template-columns: 1fr auto; grid-template-rows: 1fr auto;
      padding: 6px 10px; gap: 2px; top: 0; }
    .cabin.moving { border-color: var(--amber-dim); }
    .cabin.doors-open { border-color: var(--amber);
      box-shadow: inset 0 0 0 1px var(--amber-trace); }
    .cabin-floor { grid-column: 1; grid-row: 1 / span 2;
      font-family: var(--font-mono); font-size: 22px; font-weight: 700;
      font-variant-numeric: tabular-nums; color: var(--text-primary);
      line-height: 1; align-self: center; }
    .cabin.moving .cabin-floor, .cabin.doors-open .cabin-floor { color: var(--amber); }
    .cabin-dir { grid-column: 2; grid-row: 1; display: flex;
      align-items: center; justify-content: flex-end; color: var(--text-muted); }
    .cabin.moving .cabin-dir { color: var(--amber); }
    .cabin-doors { grid-column: 2; grid-row: 2; display: flex;
      justify-content: flex-end; gap: 2px; }
    .door { width: 4px; height: 10px; background: var(--text-muted);
      transition: transform 200ms ease, background 200ms ease; }
    .cabin.doors-open .door { background: var(--amber); }
    .cabin.doors-open .door.left { transform: translateX(-3px); }
    .cabin.doors-open .door.right { transform: translateX(3px); }
  `],
})
export class ShaftComponent {
  elevator = input.required<ElevatorDto>();
  floors = input.required<number>();
  shaftIndex = input.required<number>();

  readonly FLOOR_H = FLOOR_H;
  readonly Math = Math;

  labelIndex = computed(() => String(this.shaftIndex() + 1).padStart(2, '0'));

  cabinY = computed(() => (this.floors() - 1 - this.elevator().floor) * FLOOR_H);

  targetFloors = computed(() => {
    const e = this.elevator();
    const set = new Set<number>(e.carCalls);
    e.assignedHallCalls.forEach(h => set.add(h.floor));
    return Array.from(set);
  });

  markerY(f: number): number {
    return f * FLOOR_H + FLOOR_H / 2 - 1.5;
  }

  pad(f: number): string { return String(f).padStart(2, '0'); }
}
