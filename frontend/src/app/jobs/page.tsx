import { listJobs } from '../../lib/api';
import JobTable from '../../components/JobTable';

export default async function JobsPage() {
  const jobs = await listJobs();
  return (
    <div>
      <h1>Jobs</h1>
      <JobTable initialJobs={jobs} />
    </div>
  );
}
