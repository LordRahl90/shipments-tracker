package com.lordrahl.trendsales.daemons;

import com.lordrahl.trendsales.shipments.Shipment;
import com.lordrahl.trendsales.shipments.ShipmentService;
import com.lordrahl.trendsales.tracker.TrackingService;

import java.util.List;

public class StatusChecker implements Runnable {

    private final TrackingService trackingService;
    private final ShipmentService shipmentService;

    public StatusChecker(TrackingService trackingService, ShipmentService shipmentService) {
        this.trackingService = trackingService;
        this.shipmentService = shipmentService;
    }

    @Override
    public void run() {
        List<Shipment> shipmentList = shipmentService.undeliveredPackages();
        trackingService.checkAndProcessRemoteStatus(shipmentList);
    }
}
