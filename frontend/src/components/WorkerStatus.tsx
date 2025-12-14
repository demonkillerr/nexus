'use client';

import { useEffect, useMemo, useState } from 'react';
import type { WorkerDto } from '../lib/types';
import { subscribeToTopic } from '../lib/websocket';

export default function WorkerStatus({ initialWorkers }: { initialWorkers: WorkerDto[] }) {
  const [workers, setWorkers] = useState<WorkerDto[]>(initialWorkers);
  const byId = useMemo(() => new Map(workers.map(w => [w.id, w])), [workers]);

  useEffect(() => {
    let cancelled = false;
    let unsub: (() => void) | null = null;

    subscribeToTopic('/topic/workers', (msg) => {
      const w: WorkerDto = JSON.parse(msg.body);
      setWorkers((prev) => {
        const map = new Map(prev.map(p => [p.id, p]));
        map.set(w.id, w);
        return Array.from(map.values()).sort((a, b) => a.id.localeCompare(b.id));
      });
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
  }, []);

  return (
    <table className="table">
      <thead>
        <tr>
          <th>id</th>
          <th>status</th>
          <th>lastHeartbeat</th>
          <th>capabilities</th>
        </tr>
      </thead>
      <tbody>
        {Array.from(byId.values()).map((w) => (
          <tr key={w.id}>
            <td>{w.id}</td>
            <td>{w.status}</td>
            <td>{w.lastHeartbeat ?? '-'}</td>
            <td>{w.capabilities ?? '-'}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
