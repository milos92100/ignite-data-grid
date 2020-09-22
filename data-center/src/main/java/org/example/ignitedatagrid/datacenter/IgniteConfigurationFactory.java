package org.example.ignitedatagrid.datacenter;

import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.example.ignitedatagrid.datacenter.cache.config.CompanyCacheConfig;
import org.example.ignitedatagrid.datacenter.cache.config.UserCacheConfig;

import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import java.util.Collections;

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

    private static IgniteConfiguration common() {

        IgniteConfiguration cfg = new IgniteConfiguration();

        cfg.setPeerClassLoadingEnabled(true);

        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

        cfg.setCacheConfiguration( //
                applyCommonConfiguration(UserCacheConfig.INSTANCE), //
                applyCommonConfiguration(CompanyCacheConfig.INSTANCE) //
        );


        return cfg;
    }

    public static IgniteConfiguration forClient() {
        var cfg = common();
        cfg.setClientMode(true);
        return cfg;
    }

    public static IgniteConfiguration forServer(String name) {
        var cfg = common();
        cfg.setIgniteInstanceName(name);
        return cfg;
    }
}
