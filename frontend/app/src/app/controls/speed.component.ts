import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-speed',
  template: `
    <div class="segmented">
      @for (m of options; track m) {
        <button [class.active]="m === current" (click)="select(m)">{{ m }}×</button>
      }
    </div>
  `,
  styles: [`
    .segmented { display: flex; background: var(--surface-2);
      border: 1px solid var(--border-hairline); border-radius: var(--r-md);
      padding: 3px; gap: 1px; }
    button { all: unset; padding: 4px 10px; font-family: var(--font-mono);
      font-size: 11px; font-weight: 500; color: var(--text-tertiary); cursor: pointer;
      border-radius: 3px; transition: color 120ms ease, background 120ms ease; }
    button:hover { color: var(--text-secondary); }
    button.active { color: var(--amber); background: var(--amber-trace); }
  `],
})
export class SpeedComponent {
  readonly options = [1, 2, 5];
  @Input() current = 1;
  @Output() change = new EventEmitter<number>();
  select(m: number) { this.current = m; this.change.emit(m); }
}
