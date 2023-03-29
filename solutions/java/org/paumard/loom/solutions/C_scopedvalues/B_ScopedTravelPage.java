package org.paumard.loom.solutions.C_scopedvalues;

import jdk.incubator.concurrent.ScopedValue;
import org.paumard.loom.solutions.C_scopedvalues.B_Model.TravelPage;

public class B_ScopedTravelPage {

    public static ScopedValue<String> LICENCE_KEY = ScopedValue.newInstance();

    // --enable-preview --add-modules jdk.incubator.concurrent
    public static void main(String[] args) throws Exception {

        TravelPage travelPage =
                ScopedValue.where(LICENCE_KEY, "KEY_A")
                        .call(TravelPage::readTravelPage);

        System.out.println("Final travel page = " + travelPage);
    }
}
