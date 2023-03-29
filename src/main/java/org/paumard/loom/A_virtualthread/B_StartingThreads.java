package org.paumard.loom.A_virtualthread;

public class B_StartingThreads {

    public static void main(String[] args) throws InterruptedException {

        // You can just run this class and see what it prints.
        // The main thread is a platform thread.
        // What is the number of this thread?
        var pthread = new Thread(() -> {
            System.out.println("platform " + Thread.currentThread());
        });
        pthread.start();
        pthread.join();
    }
}
