export type Direction = 'UP' | 'DOWN' | 'IDLE';
export type ElevatorState =
  | 'IDLE' | 'MOVING' | 'DOORS_OPENING' | 'DOORS_OPEN' | 'DOORS_CLOSING';

export interface HallCallDto { floor: number; direction: 'UP' | 'DOWN'; }

export interface ElevatorDto {
  id: number;
  floor: number;
  direction: Direction;
  state: ElevatorState;
  carCalls: number[];
  assignedHallCalls: HallCallDto[];
}

export interface Snapshot {
  floors: number;
  speedMultiplier: number;
  elevators: ElevatorDto[];
  pendingHallCalls: HallCallDto[];
}
