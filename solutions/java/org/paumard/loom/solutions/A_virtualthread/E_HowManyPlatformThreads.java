package org.paumard.loom.solutions.A_virtualthread;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class E_HowManyPlatformThreads {

    public static Pattern pool = Pattern.compile("ForkJoinPool-[\\d?]");
    public static Pattern worker = Pattern.compile("worker-[\\d?]");

    // --enable-preview
    public static void main(String[] args) throws InterruptedException {

        Set<String> poolNames = ConcurrentHashMap.newKeySet();
        Set<String> platformThreadNames = ConcurrentHashMap.newKeySet();

        var threads =
                IntStream.range(0, 100)
                        .mapToObj(index ->
                                Thread.ofVirtual()
                                        .unstarted(() -> {
                                            poolNames.add(readThreadPoolName());
                                            platformThreadNames.add(readPlatformThreadName());
                                        })
                        )
                        .toList();

        Instant begin = Instant.now();
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        Instant end = Instant.now();

        System.out.println("# cores = " + Runtime.getRuntime().availableProcessors());
        System.out.println("Time = " + Duration.between(begin, end).toMillis() + "ms");
        System.out.println("### Pool names used: ");
        poolNames.forEach(System.out::println);

        System.out.println("### Platform threads used: ");
        new TreeSet<>(platformThreadNames).forEach(System.out::println);
    }

    public static String readThreadPoolName() {
        String name = Thread.currentThread().toString();
        Matcher poolMatcher = pool.matcher(name);
        if (poolMatcher.find()) {
            return poolMatcher.group();
        } else {
            return "pool not found";
        }
    }

    public static String readPlatformThreadName() {
        String name = Thread.currentThread().toString();
        Matcher workerMatcher = worker.matcher(name);
        if (workerMatcher.find()) {
            return workerMatcher.group();
        } else {
            return "platform thread not found";
        }
    }
}
