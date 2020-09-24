package org.example.ignitedatagrid.datacenter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.example.ignitedatagrid.datacenter.cache.config.UserCacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCenterServer implements LifecycleBean {

    private Ignite ignite = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCenterServer.class);

    public void start() {
        HikariConfig hikariConfig = new HikariConfig("db.properties");
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        var nodeName = System.getProperty("nodeName", "date-center");
        var config = IgniteConfigurationFactory.forServer(nodeName, dataSource);
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
