package org.paumard.loom.solutions.A_virtualthread;

import java.util.stream.IntStream;

public class D_YieldingVirtualThreads {

    // --enable-preview
    public static void main(String[] args) throws InterruptedException {

        var threads =
                IntStream.range(0, 100)
                        .mapToObj(index ->
                                Thread.ofVirtual()
                                        .unstarted(() -> {
                                            if (index == 0) {
                                                System.out.println(Thread.currentThread());
                                            }
                                            try {
                                                Thread.sleep(20);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                            if (index == 0) {
                                                System.out.println(Thread.currentThread());
                                            }
                                            try {
                                                Thread.sleep(20);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                            if (index == 0) {
                                                System.out.println(Thread.currentThread());
                                            }
                                            try {
                                                Thread.sleep(20);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                            if (index == 0) {
                                                System.out.println(Thread.currentThread());
                                            }
                                        })
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
