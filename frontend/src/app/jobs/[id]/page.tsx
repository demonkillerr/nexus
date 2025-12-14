import { getJob } from '../../../lib/api';
import JobProgress from '../../../components/JobProgress';

export default async function JobDetailPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  const job = await getJob(id);
  return (
    <div>
      <h1>Job {job.id}</h1>
      <div className="card">
        <div><b>Status:</b> {job.status}</div>
        <div><b>Worker:</b> {job.assignedWorkerId ?? '-'}</div>
        <div><b>Last %:</b> {job.lastPercent ?? 0}</div>
      </div>
      <JobProgress jobId={job.id} initialJob={job} />
    </div>
  );
}
