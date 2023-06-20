package com.lordrahl.trendsales.shipments;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentManager implements ShipmentService {
    private final ShipmentRepository shipmentRepository;

    public ShipmentManager(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public void update(Shipment shipment) {
        this.shipmentRepository.save(shipment);
    }

    @Override
    public List<Shipment> undeliveredPackages() {
        return this.shipmentRepository.undelivered();
    }

    @Override
    public void delete(Shipment shipment) {
        this.shipmentRepository.delete(shipment);
    }
}
