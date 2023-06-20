package com.lordrahl.trendsales.shipments;

import org.springframework.stereotype.Service;

import java.util.List;

public interface ShipmentService {
    void update(Shipment shipment);

    /**
     * The assumption here is that PICKED_UP is the final state.
     * and any shipment not picked up needs further processing.
     *
     * @return
     */
    List<Shipment> undeliveredPackages();

    void delete(Shipment shipment);
}
