import { Component, EventEmitter, Output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-setup',
  imports: [FormsModule],
  template: `
    <div class="config-group">
      <div class="config-field">
        <span class="config-label">FL</span>
        <input class="config-input" type="number" min="2" [(ngModel)]="floors" />
      </div>
      <div class="config-field">
        <span class="config-label">CAB</span>
        <input class="config-input" type="number" min="1" [(ngModel)]="cabins" />
      </div>
      <button class="btn btn-primary" (click)="apply.emit({floors: floors(), cabins: cabins()})">APPLY</button>
    </div>
  `,
  styles: [`
    .config-group { display: flex; align-items: center; gap: 8px; padding: 4px;
      background: var(--surface-2); border: 1px solid var(--border-hairline);
      border-radius: var(--r-md); }
    .config-field { display: flex; align-items: center; gap: 6px; padding: 2px 8px; }
    .config-label { font-family: var(--font-mono); font-size: 10px;
      letter-spacing: 0.1em; text-transform: uppercase; color: var(--text-tertiary); }
    .config-input { all: unset; width: 36px; font-family: var(--font-mono); font-size: 13px;
      font-weight: 500; color: var(--text-primary); text-align: right;
      font-variant-numeric: tabular-nums; }
    .btn { all: unset; padding: 5px 10px; font-size: 11px; font-weight: 500;
      letter-spacing: 0.04em; color: var(--text-secondary); background: var(--surface-3);
      border: 1px solid var(--border-soft); border-radius: var(--r-sm); cursor: pointer;
      transition: color 120ms ease, border-color 120ms ease; }
    .btn:hover { color: var(--text-primary); border-color: var(--border-strong); }
    .btn-primary { color: var(--amber); border-color: var(--amber-dim);
      background: var(--amber-trace); }
    .btn-primary:hover { border-color: var(--amber); }
  `],
})
export class SetupComponent {
  floors = signal(8);
  cabins = signal(3);
  @Output() apply = new EventEmitter<{ floors: number; cabins: number }>();
}
