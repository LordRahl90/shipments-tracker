package com.lordrahl.trendsales.tracker;

import com.lordrahl.trendsales.core.InternalActionService;
import com.lordrahl.trendsales.external.ShippingService;
import com.lordrahl.trendsales.shipments.PackageStatus;
import com.lordrahl.trendsales.shipments.Shipment;
import com.lordrahl.trendsales.shipments.ShipmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TrackingServiceTest {

    @Mock
    private ShipmentService shipmentService;

    @Mock
    private ShippingService shippingService;

    @Mock
    private InternalActionService internalActionService;

    @Mock
    private TrackingRepository trackingRepository;

    @Test
    public void testCheckRemoteStatus() throws IOException {
        reset(trackingRepository);
        when(shippingService.fetchStatus("1234")).thenReturn(PackageStatus.IN_TRANSIT);
        Shipment shipment = new Shipment(1L, "1234", PackageStatus.INITIATED, new Date());
        TrackingService trackingManager = new TrackingManager(shipmentService, shippingService, internalActionService, trackingRepository);

        trackingManager.checkAndProcessRemoteStatus(List.of(shipment));

        InOrder order = inOrder(shippingService, shipmentService, trackingRepository);
        order.verify(shippingService).fetchStatus(anyString());
        order.verify(trackingRepository).save(any());
        order.verify(shipmentService).update(any());
    }

    @Test
    public void testCheckRemoteStatus_MultipleShipments() throws IOException {
        reset(trackingRepository);
        when(shippingService.fetchStatus("1234")).thenReturn(PackageStatus.IN_TRANSIT);
        Shipment shipment = new Shipment(1L, "1234", PackageStatus.INITIATED, new Date());
        Shipment shipment1 = new Shipment(2L, "12345", PackageStatus.INITIATED, new Date());

        TrackingService trackingManager = new TrackingManager(shipmentService, shippingService, internalActionService, trackingRepository);

        trackingManager.checkAndProcessRemoteStatus(List.of(shipment, shipment1));

        InOrder order = inOrder(shippingService, shipmentService, trackingRepository);
        order.verify(shippingService, times(2)).fetchStatus(anyString());
        order.verify(trackingRepository, times(1)).save(any());
        order.verify(shipmentService, times(1)).update(any());
    }

    @Test
    public void testCheckRemoteStatus_MultipleShipmentValidResponse() throws IOException {
        reset(trackingRepository);
        when(shippingService.fetchStatus(anyString()))
                .thenReturn(PackageStatus.IN_TRANSIT, PackageStatus.INITIATED);
        Shipment shipment = new Shipment(1L, "1234", PackageStatus.INITIATED, new Date());
        Shipment shipment1 = new Shipment(2L, "12345", PackageStatus.INITIATED, new Date());

        TrackingService trackingManager = new TrackingManager(shipmentService, shippingService, internalActionService, trackingRepository);

        trackingManager.checkAndProcessRemoteStatus(List.of(shipment, shipment1));

        InOrder order = inOrder(shippingService, shipmentService, trackingRepository);
        order.verify(shippingService, times(2)).fetchStatus(anyString());
        order.verify(trackingRepository, times(1)).save(any());
        order.verify(shipmentService, times(2)).update(any());
    }


    @Test
    public void testProcess() throws Exception {
        reset(trackingRepository);
        List<Tracking> trackings = List.of(
                new Tracking(1L, "1234", PackageStatus.INITIATED, new Date()),
                new Tracking(2L, "1235", PackageStatus.INITIATED, new Date()),
                new Tracking(3L, "1236", PackageStatus.INITIATED, new Date())
        );
        Future<Boolean> result = CompletableFuture.supplyAsync(() -> true);

        when(trackingRepository.findAll()).thenReturn(trackings);
        when(internalActionService.perform(anyString(), any()))
                .thenReturn(result)
                .thenReturn(result)
                .thenReturn(result);

        TrackingService trackingManager = new TrackingManager(shipmentService, shippingService, internalActionService, trackingRepository);
        trackingManager.process();

        InOrder order = inOrder(internalActionService, trackingRepository);
        order.verify(internalActionService, times(3)).perform(any(), any());
        order.verify(trackingRepository, times(3)).delete(any());
    }

    @Test
    public void testProcess_WithFalseResponse() throws Exception {
        reset(trackingRepository);
        List<Tracking> trackings = List.of(
                new Tracking(1L, "1234", PackageStatus.INITIATED, new Date()),
                new Tracking(2L, "1235", PackageStatus.INITIATED, new Date()),
                new Tracking(3L, "1236", PackageStatus.INITIATED, new Date())
        );
        Future<Boolean> result = CompletableFuture.supplyAsync(() -> true);
        Future<Boolean> falseResult = CompletableFuture.supplyAsync(() -> false);

        when(trackingRepository.findAll()).thenReturn(trackings);
        when(internalActionService.perform(anyString(), any()))
                .thenReturn(result)
                .thenReturn(falseResult)
                .thenReturn(result);

        TrackingService trackingManager = new TrackingManager(shipmentService, shippingService, internalActionService, trackingRepository);
        trackingManager.process();

        InOrder order = inOrder(internalActionService, trackingRepository);
        order.verify(internalActionService, times(3)).perform(any(), any());
        order.verify(trackingRepository, times(2)).delete(any());
    }
}
