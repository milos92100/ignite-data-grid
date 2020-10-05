package org.example.ignitedatagrid.datacenter;

import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.example.ignitedatagrid.datacenter.cache.config.UserCacheConfig;

import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;

public class IgniteConfigurationFactory {

    private static <K, V> CacheConfiguration<K, V> applyCommonConfiguration(CacheConfiguration<K, V> config) {
        config.setCacheMode(CacheMode.PARTITIONED);
        config.setWriteThrough(true);
        config.setReadThrough(false);
        config.setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(Duration.ONE_HOUR));
        config.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        config.setBackups(1);
        config.setWriteBehindEnabled(true);
        config.setCopyOnRead(false);
        config.setWriteBehindFlushFrequency(10000L);
        return config;
    }

    public static IgniteConfiguration forServer(String name, TcpDiscoveryVmIpFinder ipFinder) {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName(name);
        cfg.setPeerClassLoadingEnabled(false);
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
        cfg.setCacheConfiguration( //
                applyCommonConfiguration(UserCacheConfig.create())
                //  applyCommonConfiguration(AccountCacheConfig.create()),
                //   applyCommonConfiguration(InstrumentCacheConfig.create()),
                //    applyCommonConfiguration(OrderCacheConfig.create())
        );

        return cfg;
    }
}
