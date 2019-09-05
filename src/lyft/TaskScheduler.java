package lyft;

import java.io.File;
import java.util.*;

/**
 * 每个任务有开始时间，和完成它需要的时间，每个worker只能同时做一个任务。输入是标准输入，第一行是任务数量，下面每一行是任务标号，
 * 开始时间，和完成所需时间。求一共需要多少个worker，和每一个任务是由哪个work‍‌‌‍‍‍‌‌‌‌‌‌‍‍er完成的，打印到标准输出。
 */

class Job {
    int jobId;
    int startTime;
    int endTime;

    Job(int jobId, int startTime, int endTime) {
        this.jobId = jobId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Job id:");
        sb.append(jobId);
        sb.append(" Start time:");
        sb.append(startTime);
        sb.append(" End time:");
        sb.append(endTime);
        return sb.toString();
    }
}

class Worker {
    int workerId;
    List<Job> jobList;

    Worker(int workerId) {
        this.workerId = workerId;
        this.jobList = new ArrayList<>();
    }

    public void addJob(Job job) {
        jobList.add(job);
    }

    public int getLastEndtime() {
        return jobList.isEmpty() ? -1 : jobList.get(jobList.size() - 1).endTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Worker id: ");
        sb.append(workerId);
        sb.append(" Job list: ");
        sb.append(jobList);
        return sb.toString();
    }
}

public class TaskScheduler {

    public int countWorker(Job[] jobs) {
        final PriorityQueue<Integer> pq = new PriorityQueue<>();
        Arrays.sort(jobs, (a, b) -> a.startTime != b.startTime ? a.startTime - b.startTime : a.endTime - b.endTime);
        pq.offer(jobs[0].endTime);
        for (int i = 1; i < jobs.length; i++) {
            if (jobs[i].startTime > pq.peek()) {
                pq.poll();
            }
            pq.offer(jobs[i].endTime);
        }
        return pq.size();
    }

    public Map<Integer, Integer> schduleTask(Job[] jobs) {
        // Key: job id; Value: worker id
        Map<Integer, Integer> result = new HashMap<>();
        List<Worker> workerList = new ArrayList<>();
        PriorityQueue<Worker> pq = new PriorityQueue<>((a, b) -> a.getLastEndtime() != b.getLastEndtime() ?
            a.getLastEndtime() - b.getLastEndtime() : a.workerId - b.workerId);
        workerList.add(new Worker(0));
        pq.offer(workerList.get(0));
        Worker availableWorker;

        for (Job job : jobs) {
            // There is overlap
            if (pq.peek().getLastEndtime() > job.startTime) {
                availableWorker = new Worker(workerList.size());
                workerList.add(availableWorker);
            } else {
                availableWorker = pq.poll();
            }
            availableWorker.addJob(job);
            pq.offer(availableWorker);
        }

        for (Worker worker : workerList) {
            for (Job job : worker.jobList) {
                result.put(job.jobId, worker.workerId);
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/bozhou/Dropbox/interview/src/lyft/taskinput.txt");
        Scanner sc = new Scanner(file);
        int len = Integer.valueOf(sc.nextLine());
        Job[] jobs = new Job[len];
        int index = 0;
        while (sc.hasNextLine()) {
            String[] word = sc.nextLine().split(",");
            jobs[index] = (new Job(Integer.valueOf(word[0]), Integer.valueOf(word[1]), Integer.valueOf(word[2])));
            index++;
        }
        sc.close();

        TaskScheduler ts = new TaskScheduler();
        System.out.println(ts.countWorker(jobs));
        ts.schduleTask(jobs).forEach((k,v)->System.out.println("Job id: " + k + " Worker id: " + v));
    }
}
