## Shipment Tracker Assignment

## Setup

* Clone repository
* Update `resources/application.properties` to preferred database name
* Run application

## Process

* `shipments` table is joined with `tracking` table on `packageNumber`. <br />
With some cool off period. eg, we want shipments that have not been checked in the last `1 hour` where `1 hour` is the cool off period.
* system checks the external shipping service for `PackageStatus`. If there's no change in package status, the shipment gets dropped.
* regardless of if a shipment is dropped, the `checked_at` should be updated, likewise the `current_status`
* The remaining shipments should be kept in the `tracking` table.
* A different service (`ShipmentManager`) picks up the shipments in `tracking` and feeds it to `InternalActionService`. <br />
* If the `perform` method is successful, then the record is removed from `tracking_table` otherwise, it remains to be picked up again.
* There can be multiple instances of `packageNumber` in the `tracking` table. However, there has to be for just 1 status. They will be ordered by `created_at` <br />'
Thereby making sure that all the different states are maintained

Questions:

1. Set a timeout, and treat as failure once the timeout is exceeded.
2. Set a backoff mechanism within our system as well, or send in batches based on the throttle windows.
   2a. Set a basic cool-down period within retrials.
3. Since our implementation deletes processed records from the `tracking` table, we make sure we get to try the ones that aren't deleted again.
4. This is dicey. However, making sure that deletion is done regardless of the status of the `internalAction` will ensure that we get to try only once.