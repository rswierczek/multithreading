package learning.rasw.multithreading.deadlocks;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Lock;

import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor

class Logging {

    private final Names names;
    private final Lock helmet = new ReentrantLock();
    private final Lock chainsaw = new ReentrantLock();

    Lumberjack careful() {
        return new Lumberjack(names.getRandomName(), helmet, chainsaw);
    }

    Lumberjack yolo() {
        return new Lumberjack(names.getRandomName(), chainsaw, helmet);

    }

}