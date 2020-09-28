package org.example.ignitedatagrid.datacenter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.example.ignitedatagrid.datacenter.cache.config.UserCacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class DataCenterServer implements LifecycleBean {

    private Ignite ignite = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCenterServer.class);

    public void start() {
        var dataSource = new HikariDataSource(
                new HikariConfig("db.properties")
        );

        var ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));

        var nodeName = System.getProperty("nodeName", "date-center");

        var config = IgniteConfigurationFactory.forServer(nodeName, dataSource, ipFinder);
        config.setLifecycleBeans(this);
        config.setGridLogger(new IgniteLoggerImpl(LOGGER));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Closing DB connection on shutdown...");
            dataSource.close();
            System.err.println("DB connection closed");

            if (ignite != null) {
                System.err.println("Closing Ignite on shutdown...");
                ignite.close();
                System.err.println("Ignite closed");
            }

        }));

        ignite = Ignition.start(config);

        loadCaches();
    }

    private void loadCaches() {
        ignite.getOrCreateCache(UserCacheConfig.NAME).loadCache(null);
    }

    @Override
    public void onLifecycleEvent(LifecycleEventType evt) throws IgniteException {
        LOGGER.info("Ignite: {}", evt.name());
    }
}
