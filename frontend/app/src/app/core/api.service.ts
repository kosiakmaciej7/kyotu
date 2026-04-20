import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);

  reset(floors: number, cabins: number) {
    return this.http.post('/api/building', { floors, cabins });
  }
  hallCall(floor: number, direction: 'UP' | 'DOWN') {
    return this.http.post('/api/hall-calls', { floor, direction });
  }
  carCall(elevatorId: number, floor: number) {
    return this.http.post(`/api/elevators/${elevatorId}/car-calls`, { floor });
  }
  setSpeed(multiplier: number) {
    return this.http.post('/api/simulation/speed', { multiplier });
  }
}
