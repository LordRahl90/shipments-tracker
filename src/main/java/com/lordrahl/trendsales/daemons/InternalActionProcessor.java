package com.lordrahl.trendsales.daemons;

import com.lordrahl.trendsales.tracker.TrackingService;

public class InternalActionProcessor implements Runnable {
    private final TrackingService trackingService;

    public InternalActionProcessor(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @Override
    public void run() {
        trackingService.process();
    }
}
