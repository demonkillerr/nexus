import { listWorkers } from '../../lib/api';
import WorkerStatus from '../../components/WorkerStatus';

export default async function WorkersPage() {
  const workers = await listWorkers();
  return (
    <div>
      <h1>Workers</h1>
      <WorkerStatus initialWorkers={workers} />
    </div>
  );
}
