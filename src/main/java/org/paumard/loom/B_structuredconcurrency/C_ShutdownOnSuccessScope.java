package org.paumard.loom.B_structuredconcurrency;

import org.paumard.loom.B_structuredconcurrency.C_model.Weather;

public class C_ShutdownOnSuccessScope {

    public static void main(String[] args) {

        Weather weather = Weather.readWeather();
        System.out.println("Same weather, but faster = " + weather);
    }
}
