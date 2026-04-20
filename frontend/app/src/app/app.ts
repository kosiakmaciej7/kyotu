import { Component, computed, inject, OnDestroy, OnInit } from '@angular/core';
import { ApiService } from './core/api.service';
import { StreamService } from './core/stream.service';
import { SetupComponent } from './controls/setup.component';
import { SpeedComponent } from './controls/speed.component';
import { FloorPanelComponent } from './building/floor-panel.component';
import { ShaftComponent } from './building/shaft.component';
import { CarPanelComponent } from './building/car-panel.component';
import { HallCallDto } from './core/models';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [SetupComponent, SpeedComponent, FloorPanelComponent, ShaftComponent, CarPanelComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit, OnDestroy {
  private api = inject(ApiService);
  stream = inject(StreamService);

  snapshot = this.stream.snapshot;
  connected = this.stream.connected;

  floors = computed(() => this.snapshot()?.floors ?? 0);
  elevators = computed(() => this.snapshot()?.elevators ?? []);
  speedMultiplier = computed(() => this.snapshot()?.speedMultiplier ?? 1);

  allHallCalls = computed<HallCallDto[]>(() => {
    const s = this.snapshot();
    if (!s) return [];
    const merged: HallCallDto[] = [...s.pendingHallCalls];
    s.elevators.forEach(e => merged.push(...e.assignedHallCalls));
    return merged;
  });

  activeCabins = computed(() =>
    this.elevators().filter(e => e.state !== 'IDLE').length);

  ngOnInit() { this.stream.connect(); }
  ngOnDestroy() { this.stream.disconnect(); }

  onApply(ev: { floors: number; cabins: number }) {
    this.api.reset(ev.floors, ev.cabins).subscribe();
  }
  onSpeed(m: number) { this.api.setSpeed(m).subscribe(); }
  onHallCall(ev: { floor: number; direction: 'UP' | 'DOWN' }) {
    this.api.hallCall(ev.floor, ev.direction).subscribe();
  }
  onCarCall(elevatorId: number, floor: number) {
    this.api.carCall(elevatorId, floor).subscribe();
  }
  onReset() {
    const s = this.snapshot();
    if (s) this.api.reset(s.floors, s.elevators.length).subscribe();
  }
}
