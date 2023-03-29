package org.paumard.loom.solutions.C_scopedvalues;

import jdk.incubator.concurrent.ScopedValue;

public class A_FirstScopedValue {

    // --enable-preview --add-modules jdk.incubator.concurrent
    public static void main(String[] args) {

        ScopedValue<String> scopedValue = ScopedValue.newInstance();
        Runnable task = () -> System.out.println(scopedValue.isBound() ? scopedValue.get() : "UNBOUND");

        task.run();
        ScopedValue.where(scopedValue, "AAA").run(task);
        task.run();
    }
}
