package org.paumard.loom.A_virtualthread;

public class C_StartingVirtualThreads {

    private static final Object REPLACE_WITH_YOUR_CODE = null;

    public static void main(String[] args) throws InterruptedException {

        // Let us see now how to create and launch virtual threads.
        // First, Loom brings new pattern to start platform threads.
        // Platform threads and virtual threads are running tasks,
        // instances of Runnable. So let us create a first task.
        // It just prints the current thread. The current thread is
        // the thread that is running this task.
        Runnable printCurrentThread = () -> {
            System.out.println("Current thread: " + Thread.currentThread());
        };
        // This is the new pattern Loom gives you to create and
        // launch platform threads. You need to call join() to make
        // sure that this thread has enough time to run.
        var pthread = Thread.ofPlatform()
                .name("platform-", 0)
                .start(printCurrentThread);
        pthread.join();

        // Try to find a similar pattern to start a virtual thread.
        // Create a new virtual thread, with a name "virtual", similar to
        // the previous platform thread, and start it.
        // Do not forget to call join() on this virtual thread.
        // You need to configure your IDE so that the preview features of
        // your JDK 19 are enabled.
        // How can you tell that the thread you have created is a virtual thread?
        // What platform thread is used to run this virtual thread?
        // You can also explore these two patterns.
        var vthread = REPLACE_WITH_YOUR_CODE;
    }
}
