package learning.rasw.multithreading.countdownlatch.example1;

import java.util.concurrent.CountDownLatch;

public class Driver {
    private static final int N = 10;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(N);

        for (int i = 0; i < N; ++i) { // create and start threads
            new Thread(new Worker(startSignal, doneSignal)).start();
        }

        doSomethingElse();            // don't let run yet
        startSignal.countDown();      // let all threads proceed
        doSomethingElse();
        doneSignal.await();           // wait for all to finish
    }

    private static void doSomethingElse() throws InterruptedException {
        System.out.println("[START] Driver do something else - "+ Thread.currentThread().getName());
        Thread.sleep(1000);
        System.out.println("[END] Driver do something else - "+ Thread.currentThread().getName());
    }
}
