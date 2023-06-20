package com.lordrahl.trendsales.external;

import com.lordrahl.trendsales.shipments.PackageStatus;

import java.io.IOException;

public interface ShippingService {
    /**
     * Returns the status of a shipment package
     *
     * @param packageNumber the id of the package in question
     * @throws IOException on issues with connecting to external service.
     * @return detected status
     */
    PackageStatus fetchStatus(String packageNumber) throws IOException;
}
