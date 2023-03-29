package org.paumard.loom.solutions.C_scopedvalues.B_Model;

import jdk.incubator.concurrent.StructuredTaskScope;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;


public record TravelPage(Quotation quotation, Weather weather) {

    public static final class TravelPageException extends RuntimeException {

        public TravelPageException() {
        }

        public TravelPageException(String message) {
            super(message);
        }

        public TravelPageException(Throwable cause) {
            super(cause);
        }

        public TravelPageException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static final class TravelPageScope extends StructuredTaskScope<PageComponent> {

        private Collection<Throwable> exceptions = new ConcurrentLinkedQueue<>();
        private volatile Quotation quotation;
        private volatile Weather weather;

        @Override
        protected void handleComplete(Future<PageComponent> future) {
            switch (future.state()) {
                case RUNNING -> throw new IllegalStateException("Future is still running...");
                case SUCCESS -> {
                    switch(future.resultNow()) {
                        case Quotation quotation -> this.quotation = quotation;
                        case Weather weather -> this.weather = weather;
                    }
                }
                case FAILED -> this.exceptions.add(future.exceptionNow());
                case CANCELLED -> {}
            }
        }

        public TravelPageException exceptions() {
            var travelPageException = new TravelPageException();
            this.exceptions.forEach(travelPageException::addSuppressed);
            return travelPageException;
        }

        public TravelPage travelPage() {
            if (quotation == null) {
                throw exceptions();
            }
            return new TravelPage(this.quotation, this.weather);
        }
    }

    public static TravelPage readTravelPage() {

        try (var scope = new TravelPageScope()) {

            scope.fork(Weather::readWeather);
            scope.fork(Quotation::readQuotation);

            scope.join();

            return scope.travelPage();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
