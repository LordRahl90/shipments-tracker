package com.lordrahl.trendsales;

import com.lordrahl.trendsales.daemons.InternalActionProcessor;
import com.lordrahl.trendsales.daemons.StatusChecker;
import com.lordrahl.trendsales.shipments.ShipmentService;
import com.lordrahl.trendsales.tracker.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.*;

@SpringBootApplication
public class TrendsalesApplication {

    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final TrackingService trackingService;

    private final ShipmentService shipmentService;

    public TrendsalesApplication(TrackingService trackingService, ShipmentService shipmentService) {
        this.trackingService = trackingService;
        this.shipmentService = shipmentService;
    }


    public static void main(String[] args) {
        SpringApplication.run(TrendsalesApplication.class, args);
    }


    @Bean
    public void startStatusChecker() {
        StatusChecker statusChecker = new StatusChecker(trackingService, shipmentService);
        executorService.scheduleAtFixedRate((statusChecker), 1, 10, TimeUnit.SECONDS);
    }

    @Bean
    public void startInternalActions() {
        InternalActionProcessor processor = new InternalActionProcessor(trackingService);
        executorService.scheduleAtFixedRate(processor, 1, 2,TimeUnit.SECONDS);
    }

}
