import { Component, EventEmitter, Output, computed, input } from '@angular/core';
import { HallCallDto } from '../core/models';

@Component({
  standalone: true,
  selector: 'app-floor-panel',
  template: `
    <div class="floor-column">
      @for (f of floorsDesc(); track f) {
        <div class="floor-row">
          <span class="floor-number" [class.ground]="f === 0">{{ pad(f) }}</span>
          <div class="hall-call">
            <button class="hall-btn"
                    [class.active]="hasCall(f, 'UP')"
                    [disabled]="f === floors() - 1"
                    (click)="call.emit({floor: f, direction: 'UP'})">
              <svg width="10" height="10" viewBox="0 0 10 10" fill="currentColor"><path d="M5 1.5 L8.5 7 L1.5 7 Z"/></svg>
            </button>
            <button class="hall-btn"
                    [class.active]="hasCall(f, 'DOWN')"
                    [disabled]="f === 0"
                    (click)="call.emit({floor: f, direction: 'DOWN'})">
              <svg width="10" height="10" viewBox="0 0 10 10" fill="currentColor"><path d="M5 8.5 L1.5 3 L8.5 3 Z"/></svg>
            </button>
          </div>
        </div>
      }
    </div>
  `,
  styles: [`
    .floor-column { display: flex; flex-direction: column;
      border-right: 1px solid var(--border-hairline); background: var(--surface-1); }
    .floor-row { height: var(--floor-height); display: flex; align-items: center;
      padding: 0 14px 0 16px; gap: 14px;
      border-bottom: 1px solid var(--border-hairline); }
    .floor-row:last-child { border-bottom: none; }
    .floor-number { font-family: var(--font-mono); font-size: 14px; font-weight: 500;
      font-variant-numeric: tabular-nums; color: var(--text-secondary);
      min-width: 20px; text-align: right; }
    .floor-number.ground { color: var(--amber); }
    .hall-call { display: inline-flex; flex-direction: column; background: var(--surface-2);
      border: 1px solid var(--border-hairline); border-radius: var(--r-md); overflow: hidden; }
    .hall-btn { all: unset; width: 30px; height: 22px; display: flex;
      align-items: center; justify-content: center; cursor: pointer;
      transition: background 120ms ease; color: var(--text-tertiary); }
    .hall-btn + .hall-btn { border-top: 1px solid var(--border-hairline); }
    .hall-btn:hover { color: var(--text-secondary); background: rgba(255,255,255,0.02); }
    .hall-btn.active { color: var(--amber); background: var(--amber-trace); }
    .hall-btn[disabled] { opacity: 0.2; cursor: not-allowed; }
  `],
})
export class FloorPanelComponent {
  floors = input.required<number>();
  allCalls = input<HallCallDto[]>([]);
  @Output() call = new EventEmitter<{ floor: number; direction: 'UP' | 'DOWN' }>();

  floorsDesc = computed(() => {
    const n = this.floors();
    const arr: number[] = [];
    for (let f = n - 1; f >= 0; f--) arr.push(f);
    return arr;
  });

  hasCall(floor: number, dir: 'UP' | 'DOWN'): boolean {
    return this.allCalls().some(c => c.floor === floor && c.direction === dir);
  }

  pad(f: number): string { return String(f).padStart(2, '0'); }
}
