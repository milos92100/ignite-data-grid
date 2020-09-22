package org.example.ignitedatagrid.datacenter;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.example.ignitedatagrid.datacenter.cache.config.CompanyCacheConfig;
import org.example.ignitedatagrid.datacenter.cache.config.UserCacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCenterServer implements LifecycleBean {

    private Ignite ignite = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCenterServer.class);

    public void start() {
        var nodeName = System.getProperty("nodeName", "date-center");

        var config = IgniteConfigurationFactory.forServer(nodeName);
        config.setLifecycleBeans(this);
        config.setGridLogger(new IgniteLoggerImpl(LOGGER));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (ignite == null) return;

            System.err.println("Closing Ignite on shutdown...");
            ignite.close();
            System.err.println("Ignite closed");
        }));

        ignite = Ignition.start(config);

        loadCaches();
    }

    private void loadCaches() {
        ignite.getOrCreateCache(UserCacheConfig.INSTANCE).loadCache(null);
        ignite.getOrCreateCache(CompanyCacheConfig.INSTANCE).loadCache(null);
    }

    @Override
    public void onLifecycleEvent(LifecycleEventType evt) throws IgniteException {
        LOGGER.info("Ignite: {}", evt.name());
    }
}
