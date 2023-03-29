package org.paumard.loom.solutions.B_structuredconcurrency;

import org.paumard.loom.solutions.B_structuredconcurrency.E_model.TravelPage;

public class E_BuildingTravelPage {

    // --enable-preview --add-modules jdk.incubator.concurrent
    public static void main(String[] args) {

        TravelPage travelPage = TravelPage.readTravelPage();
        System.out.println("Final travel page = " + travelPage);
    }
}
