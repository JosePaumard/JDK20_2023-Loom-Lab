package org.paumard.loom.solutions.B_structuredconcurrency;

import org.paumard.loom.solutions.B_structuredconcurrency.B_model.Weather;

public class B_FirstScope {

    // --enable-preview --add-modules jdk.incubator.concurrent
    public static void main(String[] args) {


        Weather weather = Weather.readWeather();
        System.out.println("weather = " + weather);
    }
}
