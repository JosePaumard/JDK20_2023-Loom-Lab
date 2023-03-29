package org.paumard.loom.solutions.A_virtualthread;

public class C_StartingVirtualThreads {

    private static final Object REPLACE_WITH_YOUR_CODE = null;

    // --enable-preview
    public static void main(String[] args) throws InterruptedException {

        Runnable printCurrentThread = () -> {
            System.out.println("Current thread: " + Thread.currentThread());
        };

        var pthread = Thread.ofPlatform()
                .name("platform-", 0)
                .start(printCurrentThread);
        pthread.join();

        var vthread = Thread.ofVirtual()
                .name("virtual-", 0)
                .start(printCurrentThread);
        vthread.join();
    }
}
