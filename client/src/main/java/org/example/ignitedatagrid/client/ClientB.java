package org.example.ignitedatagrid.client;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.example.ignitedatagrid.domain.entities.User;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.StreamSupport;

public class ClientB {

    public static void main(String[] args) throws IOException {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);

        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

        Ignite ignite = Ignition.start(cfg);

        IgniteCache<Long, User> cache = ignite.getOrCreateCache("UserCache");

        System.out.println("Found in cache:");
        StreamSupport //
                .stream(cache.spliterator(), false) //
                .forEach(longUserEntry -> //
                        System.out.println("key: " + longUserEntry.getKey() + "; value: " + longUserEntry.getValue()) //
                );

        // var getByFirstName = new ScanQuery<Long, User>((k, u) -> u.getFirstName().equals("fn-5"));
        // executeQuery("getByFirstName", getByFirstName, cache);


//        IgnitePredicate<Set<Address>> predicate = (addresses) -> addresses.stream().anyMatch(a -> a.getStreet().equals("Sunset blv. no5"));
//        var p = new MyPredicate();

//        var getByStreet = new ScanQuery<Long, User>((k, u) -> u.getAddresses().stream()
//                .anyMatch(address -> address.getStreet().equals("Sunset blv. no3"))
//        );
//        executeQuery("getByStreet", getByStreet, cache);

        System.out.println("Press any key to continue...");
        System.in.read();

        ignite.close();
    }

    private static void executeQuery(String name, ScanQuery<Long, User> query, IgniteCache<Long, User> cache) {
        try (var cursor = cache.query(query)) {
            System.out.println(name + " result:");
            for (var entry : cursor) {
                System.out.println("key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
        }
    }
}
