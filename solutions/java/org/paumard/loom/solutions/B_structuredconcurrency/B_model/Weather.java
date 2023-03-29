package org.paumard.loom.solutions.B_structuredconcurrency.B_model;

import jdk.incubator.concurrent.StructuredTaskScope;

import java.util.Random;

public record Weather(String agency, String weather) {

    private static final Random random = new Random();

    private static final Weather ADD_YOUR_CODE_HERE = null;

    public static final Weather UNKNOWN_WEATHER = new Weather("Unknown", "Mostly sunny");

    public static Weather readWeather() {

        try (var scope = new StructuredTaskScope<Weather>()) {

            var futureA = scope.fork(Weather::readWeatherFromA);

            scope.join();

            Weather weather = futureA.resultNow();
            return weather;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Weather readWeatherFromA() {
        try {
            Thread.sleep(random.nextInt(20, 80));
            return new Weather("Agency A", "Sunny");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Weather readWeatherFromB() {
        try {
            Thread.sleep(random.nextInt(40, 100));
            return new Weather("Agency B", "Sunny");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Weather readWeatherFromC() {
        try {
            Thread.sleep(random.nextInt(30, 120));
            return new Weather("Agency C", "Sunny");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Weather readWeatherFromD() {
        try {
            Thread.sleep(random.nextInt(10, 100));
            return new Weather("Agency D", "Sunny");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Weather readWeatherFromE() {
        try {
            Thread.sleep(random.nextInt(50, 60));
            return new Weather("Agency E", "Sunny");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
