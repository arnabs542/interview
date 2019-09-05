package dropbox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBuckets {
    // CAPACITY is the max token number we can have in the bucket
    private final int CAPACITY;
    // FILL_RATE is the number of token we will put in the bucket per second
    private final int FILL_RATE;
    private final List<Integer> bucket;
    private long lastFillTime;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public TokenBuckets(int capacity, int fillRate) {
        this.CAPACITY = capacity;
        this.FILL_RATE = fillRate;
        this.bucket = new ArrayList<>();
        this.lastFillTime = System.currentTimeMillis();
    }

    public void fill() throws InterruptedException {
        lock.lock();
        try {
            while (bucket.size() == CAPACITY) {
                System.out.println("Bucket if full now.");
                notFull.await();
            }
            long now = System.currentTimeMillis();
            long numTokensToAdd = Math.min(CAPACITY - bucket.size(), (now - lastFillTime) / 1000 * FILL_RATE);
            lastFillTime = now;
            for (int i = 0; i < numTokensToAdd; i++) {
                bucket.add((int) (Math.random() * 100) + 1);
            }
            System.out.println("Bucket is: " + bucket.toString());
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public List<Integer> get(int n) throws InterruptedException {
        if (n <= 0 || n > CAPACITY) {
            throw new IllegalArgumentException("Wrong input");
        }
        List<Integer> result = new ArrayList<>();
        int tokenGet = 0;
        while (tokenGet < n) {
            lock.lock();
            try {
                while (bucket.isEmpty()) {
                    System.out.println("Bucket is empty now");
                    notEmpty.await();
                }
                result.add(bucket.get(bucket.size() - 1));
                bucket.remove(bucket.size() - 1);
                tokenGet++;
                notFull.signalAll();
            } finally {
                lock.unlock();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        TokenBuckets tb = new TokenBuckets(100, 8);
        Runnable producer = () -> {
            while(true) {
                try {
                    tb.fill();
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        };

        Runnable consumer = () -> {
          while (true) {
              try {
                  List<Integer> result = tb.get((int) (Math.random() * 6) + 1);
                  System.out.println("consumer thread: " + Thread.currentThread().getName() + " get tokens: " + result.toString());
                  Thread.sleep(3000);
              } catch (InterruptedException ie) {
                  ie.printStackTrace();
              }
          }
        };

        new Thread(producer).start();
        new Thread(consumer).start();
        new Thread(consumer).start();
    }
}
