import { Injectable, signal } from '@angular/core';
import { Snapshot } from './models';

@Injectable({ providedIn: 'root' })
export class StreamService {
  private _snapshot = signal<Snapshot | null>(null);
  private _connected = signal(false);
  readonly snapshot = this._snapshot.asReadonly();
  readonly connected = this._connected.asReadonly();
  private source?: EventSource;

  connect() {
    if (this.source) return;
    this.source = new EventSource('/api/stream');
    this.source.addEventListener('state', (e: MessageEvent) => {
      this._snapshot.set(JSON.parse(e.data));
      this._connected.set(true);
    });
    this.source.onerror = () => this._connected.set(false);
  }

  disconnect() {
    this.source?.close();
    this.source = undefined;
    this._connected.set(false);
  }
}
