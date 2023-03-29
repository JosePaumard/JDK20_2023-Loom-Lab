package org.paumard.loom.A_virtualthread;

import java.util.stream.IntStream;

public class D_YieldingVirtualThreads {

    private static final Thread REPLACE_WITH_YOUR_CODE = null;

    public static void main(String[] args) throws InterruptedException {

        // Let us now create a bunch of virtual threads and see
        // how they can jump from one platform thread to the other.
        // This is a feature that is unique to Loom virtual threads.
        // Because blocking a Loom virtual thread is so cheap, trying
        // to pool them becomes useless.

        var threads =
                IntStream.range(0, 100)
                        .mapToObj(index ->
                                // You need to create an unstarted virtual thread here,
                                // that does the following:
                                // - if the index is 0, then print the current thread
                                // - then go to sleep for a few milliseconds, 20 is enough
                                // - if the index is 0, then print the current thread again
                                // - then go to sleep again
                                // - and then if the index is 0, print the current thread one last time
                                REPLACE_WITH_YOUR_CODE
                                // What platform thread is running your virtual thread?
                                // Can you see that your virtual thread is jumping
                                // from one platform thread to the other?
                                // Does blocking a virtual thread blocks a platform thread?
                                // Try to decrease the number of virtual threads you are creating.
                                // Do you still see this behavior?
                        )
                        .toList();

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
