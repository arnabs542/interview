package linkedin;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class DelayQueue<E extends Delayed> {
  private final transient ReentrantLock lock = new ReentrantLock();
  private final PriorityQueue<E> q = new PriorityQueue<>();

  /**
   * Thread designated to wait for the element at the head of
   * the queue.  This variant of the Leader-Follower pattern
   * (http://www.cs.wustl.edu/~schmidt/POSA/POSA2/) serves to
   * minimize unnecessary timed waiting.  When a thread becomes
   * the leader, it waits only for the next delay to elapse, but
   * other threads await indefinitely. The leader thread must
   * signal some other thread before returning from take() or
   * poll(...), unless some other thread becomes leader in the
   * interim. Whenever the head of the queue is replaced with
   * an element with an earlier expiration time, the leader
   * field is invalidated by being reset to null, and some
   * waiting thread, but not necessarily the current leader, is
   * signalled. So waiting threads must be prepared to acquire
   * and lose leadership while waiting.
   */
  private Thread leader = null;

  /**
   * Condition signalled when a newer element becomes available
   * at the head of the queue or a new thread may need to
   * become leader.
   */
  private final Condition available = lock.newCondition();

  public DelayQueue() {}

  /**
   * Inserts the specified element into this delay queue.
   * As the queue is unbounded this method will never block.
   *
   * @param e the element to add
   * @return {@code true}
   * @throws NullPointerException if the specified element is null
   */
  public boolean put(E e) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
      q.offer(e);
      /**
       * if e is the top, means the queue is empty, need to wake up all consumer threads.
       * Set the leader to null since consumer thread will compete for the leader and one
       * of them will become the new leader.
       */
      if (q.peek() == e) {
        leader = null;
        available.signal();
      }
      return true;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Retrieves and removes the head of this queue, waiting if necessary
   * until an element with an expired delay is available on this queue.
   *
   * @return the head of this queue
   * @throws InterruptedException {@inheritDoc}
   */
  public E take() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
      while(true) {
        E first = q.peek();
        if (first == null) {
          available.await();
        } else {
          long delay = first.getDelay(TimeUnit.NANOSECONDS);
          if (delay <= 0) {
            return q.poll();
          }
          /**
           * if we remove this line, consider 3 consumer thread:
           * 1. A get first, then set leader = A.thread and let A wait for a while
           * 2. B get first, then enter else wait operation, this time B holds
           *    reference to first.
           * 3. A wake up and get the element from queue. first should be GCed but
           *    it is still hold by B, so it cannot be GCed.
           * 4. If B is not wake up, then first will not be GCed, which will cause
           *    potential memory leak.
           */
          first = null; // don't retain ref while waiting
          // if leader is not null means there is already one thread
          // is getting the element from the queue. Current consumer
          // needs to wait infinitely.
          if (leader != null) {
            available.await();
          } else {
            // if leader is null means no other consumer are taking
            // elements, set the leader to current thread and let
            // consumer wait delayed time to execute the job
            Thread thisThread = Thread.currentThread();
            leader = thisThread;
            try {
              available.awaitNanos(delay);
            } finally {
              if (leader == thisThread) {
                leader = null;
              }
            }
          }
        }
      }
    } finally {
      if (leader == null && q.peek() != null) {
        available.signal();
      }
      lock.unlock();
    }
  }
}

class Task implements Delayed {
  private final String name;
  /** use milliseconds */
  private final long startTime;

  public Task(String name, long delay) {
    this.name = name;
    this.startTime = System.currentTimeMillis() + delay;
  }

  @Override
  public long getDelay(TimeUnit unit) {
    long diff = startTime - System.currentTimeMillis();
    return unit.convert(diff, TimeUnit.MILLISECONDS);
  }

  @Override
  public int compareTo(Delayed other) {
    return (int) (this.startTime - ((Task) other).startTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Task: ");
    sb.append(name);
    sb.append(" start at time: ");
    sb.append(startTime);
    return sb.toString();
  }
}

class TaskProduce implements Runnable {
  private final Random random = new Random();
  private final DelayQueue<Task> q;

  public TaskProduce(DelayQueue<Task> q) {
    this.q = q;
  }

  @Override
  public void run() {
    while (true) {
      try {
        int delay = random.nextInt(10000);
        Task task = new Task(UUID.randomUUID().toString(), delay);
        System.out.println("Thread: " + Thread.currentThread().getName() + " Put task: " + task);
        q.put(task);
        Thread.sleep(3000);
      } catch (InterruptedException ie) {
        ie.printStackTrace();
      }
    }
  }
}

class TaskConsumer implements Runnable {
  private final DelayQueue<Task> q;

  public TaskConsumer(DelayQueue<Task> q) {
    this.q = q;
  }

  @Override
  public void run() {
    while (true) {
      try {
        Task task = q.take();
        System.out.println("Thread: " + Thread.currentThread().getName() + " Take task: " + task);
      } catch (InterruptedException ie) {
        ie.printStackTrace();
      }
    }
  }
}

public class TaskScheduler {
  public static void main(String[] args) {
    DelayQueue<Task> queue = new DelayQueue<>();
    new Thread(new TaskProduce(queue), "Producer thread").start();
    new Thread(new TaskConsumer(queue), "Consumer thread 1").start();
    new Thread(new TaskConsumer(queue), "Consumer thread 2").start();
    new Thread(new TaskConsumer(queue), "Consumer thread 3").start();
  }
}
