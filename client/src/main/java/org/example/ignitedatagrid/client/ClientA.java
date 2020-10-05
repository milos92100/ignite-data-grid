package org.example.ignitedatagrid.client;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.example.ignitedatagrid.domain.entities.User;

import java.io.IOException;
import java.util.Collections;

public class ClientA {
    public static void main(String[] args) throws IOException {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);

        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

        Ignite ignite = Ignition.start(cfg);

        writeToStream(ignite);

        System.out.println("Created the cache and add the values.");
        System.out.println("Press any key to continue...");
        System.in.read();

        ignite.close();
    }

    public static void writeToStream(Ignite ignite) {
        IgniteCache<Long, User> userCache = ignite.getOrCreateCache("UserCache");
        IgniteDataStreamer<Long, User> userStreamer = ignite.dataStreamer(userCache.getName());
        userStreamer.allowOverwrite(true);
        userStreamer.receiver((cache, entries) ->
                entries.forEach(entry -> cache.put(entry.getKey(), entry.getValue()))
        );


        IgniteCache<Long, User> accountCache = ignite.getOrCreateCache("AccountCache");

        long started = System.currentTimeMillis();
        System.out.println("Started writing");
        for (long i = 1; i < 100000; i++) {
            var user = new User(i, "fn-" + i, "ln-" + i);
            userStreamer.addData(i, user);
            if (i % 1000 == 0) {
                userStreamer.flush();
                System.out.println("flushed at " + i + " record");
            }
        }
        userStreamer.close();

        System.out.println("Finished writing in " + (System.currentTimeMillis() - started) + " ms");
    }

    public static void writeNormal(Ignite ignite) {
        IgniteCache<Long, User> userCache = ignite.getOrCreateCache("UserCache");

        long started = System.currentTimeMillis();
        System.out.println("Started writing");
        for (long i = 1; i < 100000; i++) {
            var user = new User(i, "fn-" + i, "ln-" + i);
            userCache.put(i, user);
        }

        System.out.println("Finished writing in " + (System.currentTimeMillis() - started) + " ms");
    }
}
