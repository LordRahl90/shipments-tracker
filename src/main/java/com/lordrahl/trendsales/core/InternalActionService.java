package com.lordrahl.trendsales.core;

import com.lordrahl.trendsales.shipments.PackageStatus;

import java.io.IOException;
import java.util.concurrent.Future;

public interface InternalActionService {
    /**
     * Perfect the appropriate action for a given shipping event (async)
     *
     * @param packageNumber the id of the package in question
     * @param status the current status of the package
     * @throws IOException on issues with connecting to external service.
     * @return a future boolean (true for success, false for failure)
     *
     */
    Future<Boolean> perform(String packageNumber, PackageStatus status) throws Exception;
}
