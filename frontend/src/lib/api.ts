import type { JobDto, WorkerDto } from './types';

const baseUrl = process.env.NEXT_PUBLIC_SCHEDULER_HTTP_URL ?? 'http://localhost:8080';

export async function listJobs(): Promise<JobDto[]> {
  const res = await fetch(`${baseUrl}/api/jobs`, { cache: 'no-store' });
  if (!res.ok) throw new Error('Failed to load jobs');
  return res.json();
}

export async function getJob(id: string): Promise<JobDto> {
  const res = await fetch(`${baseUrl}/api/jobs/${id}`, { cache: 'no-store' });
  if (!res.ok) throw new Error('Failed to load job');
  return res.json();
}

export async function submitJob(parameters: Record<string, unknown>): Promise<JobDto> {
  const res = await fetch(`${baseUrl}/api/jobs`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ parameters })
  });
  if (!res.ok) throw new Error('Failed to submit job');
  return res.json();
}

export async function listWorkers(): Promise<WorkerDto[]> {
  const res = await fetch(`${baseUrl}/api/workers`, { cache: 'no-store' });
  if (!res.ok) throw new Error('Failed to load workers');
  return res.json();
}
