package com.lordrahl.trendsales.tracker;

import com.lordrahl.trendsales.core.InternalActionService;
import com.lordrahl.trendsales.external.ShippingService;
import com.lordrahl.trendsales.shipments.PackageStatus;
import com.lordrahl.trendsales.shipments.Shipment;
import com.lordrahl.trendsales.shipments.ShipmentService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class TrackingManager implements TrackingService {
    private final ShipmentService shipmentService;
    private final ShippingService shippingService;

    private final InternalActionService internalActionService;
    private final TrackingRepository trackingRepository;

    public TrackingManager(ShipmentService shipmentService, ShippingService shippingService, InternalActionService internalActionService, TrackingRepository trackingRepository) {
        this.shipmentService = shipmentService;
        this.shippingService = shippingService;
        this.internalActionService = internalActionService;
        this.trackingRepository = trackingRepository;
    }

    /**
     * Fetch the external status.
     * Compare the status, and add if it changes.
     * We filter out the shipments that their lists have changed
     *
     * @param shipments
     */
    @Override
    public void checkAndProcessRemoteStatus(List<Shipment> shipments) {
        shipments.stream().parallel().forEach(this::checkRemoteStatus);
    }

    @Override
    public void updatePackageStatusCheckedAt(Shipment shipment, PackageStatus status) {
        shipment.setCheckedAt(new Date());
        shipment.setCurrentStatus(status);
        this.shipmentService.update(shipment);
    }

    @Override
    public List<Tracking> loadUnprocessed() {
        return this.trackingRepository.findAll();
    }

    /**
     * Loads unprocessed tracking records and passes them on the internal action service
     */
    @Override
    public void process() {
        List<Tracking> trackings = this.loadUnprocessed();
        trackings.parallelStream().forEach(tracking -> {
            if (perform(tracking)) {
                // we remove the element from the tracking table
                delete(tracking);
            }
        });
    }

    public void delete(Tracking tracking) {
        this.trackingRepository.delete(tracking);
    }

    private void checkRemoteStatus(Shipment shipment) {
        try {
            PackageStatus status = this.shippingService.fetchStatus(shipment.getPackageNumber());
            if (status == null) {
                return;
            }

            if (status != shipment.getCurrentStatus()) {
                this.trackingRepository.save(new Tracking(null, shipment.getPackageNumber(), status, null));
            }
            // update the status to the current one and persist if not null
            this.updatePackageStatusCheckedAt(shipment, status);
        } catch (Exception ex) {
            // handle or rethrow the error cleanly here
            ex.printStackTrace();
        }
    }

    private Boolean perform(Tracking tracking) {
        try {
            Future<Boolean> result = internalActionService.perform(tracking.getPackageNumber(), tracking.getPackageStatus());
            if (result.isCancelled()) {
                return false;
            }
            return result.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
