package org.paumard.loom.C_scopedvalues;

import org.paumard.loom.B_structuredconcurrency.E_model.TravelPage;

public class B_ScopedTravelPage {

    // --enable-preview --add-modules jdk.incubator.concurrent
    public static void main(String[] args) throws Exception {

        // Modify this code to set a licence key and prevent the construction
        // of your Quotation object if the key is not there
        TravelPage travelPage = TravelPage.readTravelPage();
        System.out.println("Final travel page = " + travelPage);
    }
}
