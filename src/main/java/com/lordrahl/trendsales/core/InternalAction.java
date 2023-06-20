package com.lordrahl.trendsales.core;

import com.lordrahl.trendsales.shipments.PackageStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class InternalAction implements InternalActionService {

    @Override
    public Future<Boolean> perform(String packageNumber, PackageStatus status) throws Exception {
        return null;
    }
}
