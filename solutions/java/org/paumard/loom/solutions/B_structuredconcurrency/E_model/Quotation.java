package org.paumard.loom.solutions.B_structuredconcurrency.E_model;

import jdk.incubator.concurrent.StructuredTaskScope;
import org.paumard.loom.solutions.B_structuredconcurrency.E_model.PageComponent;

import java.util.Collection;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

public record Quotation(String agency, int amount) implements PageComponent {

    private static final Quotation ADD_YOUR_CODE_HERE = null;
    private static Random random = new Random();

    public static class QuotationException extends RuntimeException {

        public QuotationException() {
        }

        public QuotationException(Throwable cause) {
            super(cause);
        }

        public QuotationException(String message) {
            super(message);
        }

        public QuotationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class QuotationScope extends StructuredTaskScope<Quotation> {

        private final Collection<Quotation> quotations = new ConcurrentLinkedQueue<>();
        private final Collection<Throwable> exceptions = new ConcurrentLinkedQueue<>();

        @Override
        protected void handleComplete(Future<Quotation> future) {
            switch (future.state()) {
                case RUNNING -> throw new IllegalStateException("Future is still running...");
                case SUCCESS -> this.quotations.add(future.resultNow());
                case FAILED -> this.exceptions.add(future.exceptionNow());
                case CANCELLED -> {}
            }
        }

        public QuotationException exceptions() {
            QuotationException exception = new QuotationException();
            this.exceptions.forEach(exception::addSuppressed);
            return exception;
        }

        public Quotation bestQuotation() {
            return this.quotations.stream()
                    .min(Comparator.comparing(Quotation::amount))
                    .orElseThrow(this::exceptions);
        }
    }

    public static Quotation readQuotation() {

        try (var scope = new QuotationScope()) {

            scope.fork(Quotation::readQuotationFromA);
            scope.fork(Quotation::readQuotationFromB);
            scope.fork(Quotation::readQuotationFromC);

            scope.join();

            Quotation bestQuotation = scope.bestQuotation();
            return bestQuotation;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Quotation readQuotationFromA() {
        try {
            Thread.sleep(random.nextInt(20, 80));
            return new Quotation("Agency A", random.nextInt(80, 120));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Quotation readQuotationFromB() {
        try {
            Thread.sleep(random.nextInt(40, 100));
            return new Quotation("Agency B", random.nextInt(90, 130));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Quotation readQuotationFromC() {
        try {
            Thread.sleep(random.nextInt(30, 120));
            return new Quotation("Agency C", random.nextInt(70, 130));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Quotation readQuotationFromD() {
        try {
            Thread.sleep(random.nextInt(20, 110));
            return new Quotation("Agency D", random.nextInt(60, 120));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Quotation readQuotationFromE() {
        try {
            Thread.sleep(random.nextInt(50, 90));
            return new Quotation("Agency E", random.nextInt(70, 110));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
