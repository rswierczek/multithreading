package learning.rasw.multithreading.countdownlatch.example1;

import java.util.concurrent.CountDownLatch;

class Worker implements Runnable {
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }
    public void run() {
        try {
            startSignal.await(); //Causes the current thread to wait until the latch has counted down to zero
            doWork();
            doneSignal.countDown(); //Decrements the count of the latch, releasing all waiting threads if the count reaches zero.
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    void doWork() throws InterruptedException {
        System.out.println("[START] Worker do work - " + Thread.currentThread().getName());
        Thread.sleep(3000);
        System.out.println("[END] Worker do work - "+ Thread.currentThread().getName());
    }
}