package dropbox;

import java.util.*;
import java.util.concurrent.*;

/**
 *
 */
public class WebCrawler {
    public List<String> getUrls(String urlStr) {
        // provided API to use
        return new ArrayList<>();
    }

    public List<String> crawlBfs(String url) {
        Set<String> result = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        queue.offer(url);
        result.add(url);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            List<String> childUrls = getUrls(current);
            if (!childUrls.isEmpty()) {
                for (String childUrl : childUrls) {
                    if (!result.contains(childUrl)) {
                        queue.offer(childUrl);
                        result.add(childUrl);
                    }
                }
            }
        }
        return new ArrayList<>(result);
    }

    /**
     * Multithreaded version of crawler
     */
    public static final int THREAD_NUM = 10;
    private static final long PAUSE_TIME = 1000; // in milliseconds

    private final Set<String> result = new HashSet<>();
    private final List<Future<List<String>>> futures = new ArrayList<>();
    private ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUM);

    public static Map<String, List<String>> connectedUrls;

    public List<String> crawl(String url) {
        submitNewUrl(url);
        try {
            while (checkCrawlerResult()) {}
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        executor.shutdown();
        return new ArrayList<>(result);
    }

    private boolean checkCrawlerResult() throws InterruptedException {
        Thread.sleep(PAUSE_TIME);
        Iterator<Future<List<String>>> iterator = futures.iterator();
        Set<String> newUrls = new HashSet<>();
        while (iterator.hasNext()) {
            Future<List<String>> future = iterator.next();
            if (future.isDone()) {
                iterator.remove();
                try {
                    newUrls.addAll(future.get());
                } catch (ExecutionException ee) {
                    ee.printStackTrace();
                }
            }
        }

        for (String url : newUrls) {
            submitNewUrl(url);
        }

        return futures.size() > 0;
    }

    private void submitNewUrl(String url) {
        if (!result.contains(url)) {
            result.add(url);
            // crate a new callable crawler and execute to get the result list
            Crawler crawler = new Crawler(url);
            Future<List<String>> future = executor.submit(crawler);
            futures.add(future);
        }
    }

    class Crawler implements Callable<List<String>> {
        private final String url;

        Crawler(String url) {
            this.url = url;
        }

        @Override
        public List<String> call() {
            List<String> urls = getUrlsUpdated(url);
            return urls;
        }
    }

    private List<String> getUrlsUpdated(String url) {
        return connectedUrls.getOrDefault(url, new ArrayList<>());
    }

    public static void main(String[] args) {
        connectedUrls = new HashMap<>();
        List<String> aChildren = new ArrayList<>();
        aChildren.add("b");
        aChildren.add("c");
        aChildren.add("d");
        aChildren.add("e");
        List<String> bChildren = new ArrayList<>();
        bChildren.add("k");
        bChildren.add("m");
        bChildren.add("d");
        bChildren.add("z");
        List<String> kChildren = new ArrayList<>();
        kChildren.add("o");
        kChildren.add("j");
        kChildren.add("e");
        kChildren.add("z");

        connectedUrls.put("a", aChildren);
        connectedUrls.put("b", bChildren);
        connectedUrls.put("k", kChildren);

        WebCrawler crawler = new WebCrawler();
        System.out.println(crawler.crawl("a"));
    }
}
