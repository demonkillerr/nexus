'use client';

import { useEffect, useState } from 'react';
import type { JobDto } from '../lib/types';
import { subscribeToTopic } from '../lib/websocket';

export default function JobProgress({ jobId, initialJob }: { jobId: string; initialJob: JobDto }) {
  const [job, setJob] = useState<JobDto>(initialJob);

  useEffect(() => {
    let cancelled = false;
    let unsub: (() => void) | null = null;

    subscribeToTopic(`/topic/jobs/${jobId}`, (msg) => {
      setJob(JSON.parse(msg.body));
    }).then((sub) => {
      if (cancelled) {
        sub.unsubscribe();
      } else {
        unsub = sub.unsubscribe;
      }
    }).catch(console.error);

    return () => {
      cancelled = true;
      if (unsub) unsub();
    };
  }, [jobId]);

  const percent = job.lastPercent ?? 0;
  return (
    <div className="card">
      <h2>Live progress</h2>
      <div className="progressOuter">
        <div className="progressInner" style={{ width: `${Math.min(100, Math.max(0, percent))}%` }} />
      </div>
      <div>{percent.toFixed(1)}%</div>
      <div><b>Status:</b> {job.status}</div>
    </div>
  );
}
