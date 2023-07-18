package com.seniors.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {

        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (isReadOnly) {
            log.info("Slave DataSource 호출 => ");
        } else {
            log.info("Master DataSource 호출");
        }
        return isReadOnly ? "slave" : "master";
    }
}
