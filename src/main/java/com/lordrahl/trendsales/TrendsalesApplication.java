package com.lordrahl.trendsales;

import com.lordrahl.trendsales.tracker.TrackingManager;
import com.lordrahl.trendsales.tracker.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.*;

@SpringBootApplication
public class TrendsalesApplication {

    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

//    public TrendsalesApplication(TrackingService trackingService) {
//        this.trackingService = trackingService;
//    }

    public static void main(String[] args) {
//        TrackingManager manager = new TrackingManager()
        executorService.scheduleAtFixedRate(() -> {
            System.out.println("hello world");
//            this.trackingService.start();
        }, 1, 2, TimeUnit.SECONDS);
        SpringApplication.run(TrendsalesApplication.class, args);
    }


//    public CommandLineRunner commandLineRunner = new CommandLineRunner() {
//        @Override
//        public void run(String... args) throws Exception {
//
//        }
//    };

}
