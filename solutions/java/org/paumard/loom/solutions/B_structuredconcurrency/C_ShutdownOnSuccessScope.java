package org.paumard.loom.solutions.B_structuredconcurrency;

import org.paumard.loom.solutions.B_structuredconcurrency.B_model.Weather;

public class C_ShutdownOnSuccessScope {

    // --enable-preview --add-modules jdk.incubator.concurrent
    public static void main(String[] args) {

        Weather weather = Weather.readWeather();
        System.out.println("Same weather, but faster = " + weather);
    }
}
