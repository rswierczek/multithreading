package learning.rasw.multithreading.countdownlatch.example2;

import java.util.concurrent.CountDownLatch;

public class WorkerRunnable implements Runnable {
    private final CountDownLatch doneSignal;
    private final int i;


    WorkerRunnable(CountDownLatch doneSignal, int i) {
        this.doneSignal = doneSignal;
        this.i = i;
    }

    public void run() {
        try {
            doWork(i);
            doneSignal.countDown();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    void doWork(int i) throws InterruptedException {
        System.out.println("[START] Worker do work - " + Thread.currentThread().getName());
        Thread.sleep(3000);
        System.out.println("[END] Worker do work - " + Thread.currentThread().getName());
    }
}

