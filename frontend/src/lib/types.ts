export type JobStatus = 'CREATED' | 'QUEUED' | 'RUNNING' | 'COMPLETED' | 'FAILED' | 'CANCELED';

export type JobDto = {
  id: string;
  status: JobStatus;
  createdAt?: string;
  completedAt?: string;
  assignedWorkerId?: string | null;
  lastPercent?: number | null;
  parameters?: string;
};

export type WorkerStatus = 'UP' | 'DOWN';

export type WorkerDto = {
  id: string;
  status: WorkerStatus;
  lastHeartbeat?: string;
  capabilities?: string;
};
