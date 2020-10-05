package org.example.ignitedatagrid.datacenter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.internal.IgnitionEx;
import org.apache.ignite.internal.processors.resource.GridSpringResourceContextImpl;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.example.ignitedatagrid.datacenter.cache.config.AccountCacheConfig;
import org.example.ignitedatagrid.datacenter.cache.config.InstrumentCacheConfig;
import org.example.ignitedatagrid.datacenter.cache.config.OrderCacheConfig;
import org.example.ignitedatagrid.datacenter.cache.config.UserCacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Collections;

public class DataCenterServer implements LifecycleBean {

    private Ignite ignite = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataCenterServer.class);

    public void start() {
        try {
            var dataSource = new HikariDataSource(
                    new HikariConfig("db.properties")
            );

            var ipFinder = new TcpDiscoveryMulticastIpFinder();
            ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));

            var nodeName = System.getProperty("nodeName", "date-center");

            var config = IgniteConfigurationFactory.forServer(nodeName, ipFinder);
            config.setLifecycleBeans(this);
            config.setGridLogger(new IgniteLoggerImpl(LoggerFactory.getLogger(nodeName)));

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

            var appContext = new GenericApplicationContext();
            appContext.getBeanFactory().registerSingleton("dataSource", dataSource);
            appContext.refresh();

            var springCtx = new GridSpringResourceContextImpl(appContext);

            ignite = IgnitionEx.start(config, springCtx);

            loadCaches();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void loadCaches() {
        loadCacheWithMetrics(ignite.getOrCreateCache(UserCacheConfig.NAME));
        loadCacheWithMetrics(ignite.getOrCreateCache(AccountCacheConfig.NAME));
        loadCacheWithMetrics(ignite.getOrCreateCache(InstrumentCacheConfig.NAME));
        loadCacheWithMetrics(ignite.getOrCreateCache(OrderCacheConfig.NAME));
    }

    private void loadCacheWithMetrics(IgniteCache<?, ?> cache) {
        long now = System.currentTimeMillis();
        LOGGER.info("Started loading cache {}", cache.getName());
        cache.loadCache(null);
        LOGGER.info("{} cache loaded after: {} ms", cache.getName(), System.currentTimeMillis() - now);
    }

    @Override
    public void onLifecycleEvent(LifecycleEventType evt) throws IgniteException {
        LOGGER.info("Ignite: {}", evt.name());
    }
}
