package org.paumard.loom.B_structuredconcurrency;

import org.paumard.loom.B_structuredconcurrency.B_model.Weather;

public class B_FirstScope {

    public static void main(String[] args) {

        // Let us first explore the StructuredTaskScope class. This class is your main
        // entry point for structured concurrency with Loom.
        // Go ahead and visit the Weather record, and follow the instructions
        // to implement the readWeather() method.
        // Once you are done with the instructions in the Weather class, you can
        // run this main method.
        // Do not forget to add the following options to run this main method:
        // --enable-preview --add-modules jdk.incubator.concurrent

        Weather weather = Weather.readWeather();
        System.out.println("weather = " + weather);
    }
}
