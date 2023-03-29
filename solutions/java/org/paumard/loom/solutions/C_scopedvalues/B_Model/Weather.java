package org.paumard.loom.solutions.C_scopedvalues.B_Model;

import jdk.incubator.concurrent.StructuredTaskScope;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public record Weather(String agency, String weather) implements PageComponent {

    private static final Random random = new Random();

    public static final Weather UNKNOWN_WEATHER = new Weather("Unknown", "Mostly sunny");

    public static Weather readWeather() {

        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Weather>()) {

            scope.fork(Weather::readWeatherFromA);
            scope.fork(Weather::readWeatherFromB);
            scope.fork(Weather::readWeatherFromC);

            scope.joinUntil(Instant.now().plusMillis(100));

            Weather weather = scope.result();
            return weather;

        } catch (InterruptedException | ExecutionException e) {
            return UNKNOWN_WEATHER;
        } catch (TimeoutException e) {
            return UNKNOWN_WEATHER;
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
