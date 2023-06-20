package com.lordrahl.trendsales.shipments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long>
{
    /**
     * TODO: Think about this more, if it makes sense to add a where clause to the subquery
     * Fetches the shipments that have not been delivered and are also not processed.
     *
     * @return Lis<Shipment></Shipment>
     */
    @Query("SELECT s FROM Shipment s WHERE packageNumber NOT IN (SELECT t.packageNumber FROM Tracking t)")
    List<Shipment> undelivered();
}
