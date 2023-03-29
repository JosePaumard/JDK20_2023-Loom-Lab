package org.paumard.loom.A_virtualthread;

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

    private static final Thread REPLACE_WITH_YOUR_CODE = null;

    public static void main(String[] args) throws InterruptedException {

        // Let us discover how many platform threads you need
        // to run your virtual threads.

        // Here are two sets, one to store the name of the pools of
        // platform threads that are used by default, and the other
        // to store the name of the platform threads.

        Set<String> poolNames = ConcurrentHashMap.newKeySet();
        Set<String> platformThreadNames = ConcurrentHashMap.newKeySet();

        var threads =
                IntStream.range(0, 100)
                        .mapToObj(index ->
                                // Just call the two methods readThreadPoolName() and
                                // readPlatformThreadName() and add the pool name and the platform
                                // thread name in the corresponding sets.
                                REPLACE_WITH_YOUR_CODE
                                // How many pools is Loom using?
                                // How many platform threads have been used for your virtual threads?
                                // You can try to increase the number of virtual threads, to see if
                                // this number varies.
                                // You can also try to run this code on a machine
                                // with a different number of cores, to see how this number is changing.
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
