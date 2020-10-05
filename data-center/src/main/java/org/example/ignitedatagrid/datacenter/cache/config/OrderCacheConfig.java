package org.example.ignitedatagrid.datacenter.cache.config;

import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.configuration.CacheConfiguration;
import org.example.ignitedatagrid.datacenter.factory.OrderAdapterFactory;
import org.example.ignitedatagrid.domain.entities.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class OrderCacheConfig extends CacheConfiguration<Long, Order> {
    public static final String NAME = "OrderCache";

    public static OrderCacheConfig create() {
        var config = new OrderCacheConfig();
        var queryEntity = new QueryEntity();
        queryEntity.setKeyFieldName("id");
        queryEntity.setValueType(Order.class.getName());

        var fields = new LinkedHashMap<String, String>();
        fields.put("id", Long.class.getName());
        fields.put("volume", Integer.class.getName());
        fields.put("price", Double.class.getName());
        fields.put("account_id", Long.class.getName());
        fields.put("instrument_id", Long.class.getName());
        queryEntity.setFields(fields);

        var indexes = new ArrayList<QueryIndex>(3);
        indexes.add(new QueryIndex("id"));
        indexes.add(new QueryIndex("account_id"));
        indexes.add(new QueryIndex("instrument_id"));
        queryEntity.setIndexes(indexes);

        config.setName(NAME);
        config.setQueryEntities(Collections.singletonList(queryEntity));
        config.setCacheStoreFactory(new OrderAdapterFactory());

        return config;
    }
}
