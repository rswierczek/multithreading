package learning.rasw.multithreading.deadlocks;

import com.github.javafaker.Faker;

public class Names {
    private Faker faker = new Faker();

    public String getRandomName() {
        return faker.name().fullName();
    }
}
