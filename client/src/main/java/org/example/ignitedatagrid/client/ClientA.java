package org.example.ignitedatagrid.client;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.example.ignitedatagrid.domain.Address;
import org.example.ignitedatagrid.domain.User;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class ClientA {
    public static void main(String[] args) throws IOException {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);

        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

        Ignite ignite = Ignition.start(cfg);

        IgniteCache<Long, User> userCache = ignite.getOrCreateCache("UserCache");


        for (long i = 1; i < 100; i++) {

            var address = new Address("California", "Polo alto", "Sunset blv. no" + i);

            var user = new User(i, "fn-" + i, "ln-" + i, Set.of(address), 1L);
            userCache.put(i, user);
        }

        System.out.println("Created the cache and add the values.");
        System.out.println("Press any key to continue...");
        System.in.read();

        ignite.close();
    }
}
