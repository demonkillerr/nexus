'use client';

import { useMemo, useState } from 'react';
import type { JobDto } from '../lib/types';
import { submitJob } from '../lib/api';

export default function JobTable({ initialJobs }: { initialJobs: JobDto[] }) {
  const [jobs, setJobs] = useState<JobDto[]>(initialJobs);
  const [steps, setSteps] = useState(120);
  const [sleepMs, setSleepMs] = useState(50);
  const rows = useMemo(() => jobs, [jobs]);

  async function onSubmit() {
    const created = await submitJob({ steps, sleepMs });
    setJobs((prev) => [created, ...prev]);
  }

  return (
    <div>
      <div className="card">
        <h2>Submit job</h2>
        <div className="row">
          <label>steps</label>
          <input type="number" value={steps} onChange={(e) => setSteps(Number(e.target.value))} />
          <label>sleepMs</label>
          <input type="number" value={sleepMs} onChange={(e) => setSleepMs(Number(e.target.value))} />
          <button onClick={onSubmit}>Submit</button>
        </div>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>id</th>
            <th>status</th>
            <th>worker</th>
            <th>last %</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((j) => (
            <tr key={j.id}>
              <td><a href={`/jobs/${j.id}`}>{j.id}</a></td>
              <td>{j.status}</td>
              <td>{j.assignedWorkerId ?? '-'}</td>
              <td>{(j.lastPercent ?? 0).toFixed(1)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
