import { Component, EventEmitter, Output, computed, input } from '@angular/core';
import { ElevatorDto } from '../core/models';

@Component({
  standalone: true,
  selector: 'app-car-panel',
  template: `
    <div class="car-panel">
      <div class="car-panel-header">
        <span class="car-panel-title">SHAFT {{ labelIndex() }} · CAR PANEL</span>
        <span class="car-panel-readout">
          <span class="num" [class.active]="elevator().state !== 'IDLE'">{{ pad(floorDisplay()) }}</span>
          <span class="dot">·</span>
          <span>{{ statusText() }}</span>
        </span>
      </div>
      <div class="car-pad">
        @for (f of floorList(); track f) {
          <button class="car-btn" [class.selected]="elevator().carCalls.includes(f)"
                  (click)="pick.emit(f)">{{ pad(f) }}</button>
        }
      </div>
    </div>
  `,
  styles: [`
    .car-panel { background: var(--surface-1);
      border: 1px solid var(--border-hairline); border-radius: var(--r-lg);
      padding: 16px; }
    .car-panel-header { display: flex; align-items: center;
      justify-content: space-between; margin-bottom: 12px; }
    .car-panel-title { font-family: var(--font-mono); font-size: 10px;
      font-weight: 600; letter-spacing: 0.16em; text-transform: uppercase;
      color: var(--text-secondary); }
    .car-panel-readout { font-family: var(--font-mono); font-size: 11px;
      color: var(--text-tertiary); display: flex; gap: 6px; align-items: center; }
    .car-panel-readout .num { color: var(--text-primary); font-weight: 600; }
    .car-panel-readout .num.active { color: var(--amber); }
    .car-panel-readout .dot { color: var(--text-muted); }
    .car-pad { display: grid; grid-template-columns: repeat(4, 1fr); gap: 6px;
      padding: 10px; background: var(--surface-2);
      border: 1px solid var(--border-hairline); border-radius: var(--r-md); }
    .car-btn { all: unset; aspect-ratio: 1; display: flex; align-items: center;
      justify-content: center; font-family: var(--font-mono); font-size: 12px;
      font-weight: 500; font-variant-numeric: tabular-nums; color: var(--text-secondary);
      background: var(--surface-3); border: 1px solid var(--border-hairline);
      border-radius: var(--r-sm); cursor: pointer;
      transition: color 120ms ease, border-color 120ms ease, background 120ms ease; }
    .car-btn:hover { color: var(--text-primary); border-color: var(--border-soft); }
    .car-btn.selected { color: var(--amber); border-color: var(--amber-dim);
      background: var(--amber-trace);
      box-shadow: inset 0 0 0 1px var(--amber-trace); }
  `],
})
export class CarPanelComponent {
  elevator = input.required<ElevatorDto>();
  floors = input.required<number>();
  shaftIndex = input.required<number>();
  @Output() pick = new EventEmitter<number>();

  labelIndex = computed(() => String(this.shaftIndex() + 1).padStart(2, '0'));

  floorList = computed(() => {
    const n = this.floors();
    return Array.from({ length: n }, (_, i) => i);
  });

  floorDisplay = computed(() => Math.floor(this.elevator().floor));

  statusText = computed(() => {
    const e = this.elevator();
    if (e.state === 'MOVING') return e.direction;
    if (e.state === 'DOORS_OPEN') return 'DOORS OPEN';
    if (e.state === 'DOORS_OPENING') return 'DOORS OPENING';
    if (e.state === 'DOORS_CLOSING') return 'DOORS CLOSING';
    return 'IDLE';
  });

  pad(f: number): string { return String(f).padStart(2, '0'); }
}
