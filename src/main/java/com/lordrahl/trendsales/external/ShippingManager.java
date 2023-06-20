package com.lordrahl.trendsales.external;

import com.lordrahl.trendsales.shipments.PackageStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ShippingManager implements ShippingService{

    @Override
    public PackageStatus fetchStatus(String packageNumber) throws IOException {
        return null;
    }
}
