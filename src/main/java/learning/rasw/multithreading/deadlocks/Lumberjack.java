package learning.rasw.multithreading.deadlocks;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Lock;

@RequiredArgsConstructor
class Lumberjack {

    private final String name;
    private final Lock accessoryOne;
    private final Lock accessoryTwo;

    void cut(Runnable work) {

        try {
            accessoryOne.lock();
            try {
                accessoryTwo.lock();
                work.run();
            } finally {
                accessoryTwo.unlock();
            }

        } finally {
            accessoryOne.unlock();

        }

    }

}
