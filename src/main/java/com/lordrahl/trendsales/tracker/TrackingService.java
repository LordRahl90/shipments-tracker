package com.lordrahl.trendsales.tracker;

import com.lordrahl.trendsales.shipments.PackageStatus;
import com.lordrahl.trendsales.shipments.Shipment;

import java.util.List;

public interface TrackingService {
    /**
     * Checks for shipments that their status have changed.
     * <p>
     * This checks/polls the external ShipmentService for status of each shipment.
     * and updates the checked_at and current_status of each shipment.
     *
     * @param shipments
     */
    void checkAndProcessRemoteStatus(List<Shipment> shipments);

    void updatePackageStatusCheckedAt(Shipment shipment, PackageStatus status);

    List<Tracking> loadUnprocessed();

    // process calls the shipping service.
    // if the call is successful, the shipment is removed from the `tracking_table`
    void process();
}
