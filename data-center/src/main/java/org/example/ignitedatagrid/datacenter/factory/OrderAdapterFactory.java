package org.example.ignitedatagrid.datacenter.factory;

import org.example.ignitedatagrid.datacenter.adapter.OrderCacheStoreAdapter;

import javax.cache.configuration.Factory;

public class OrderAdapterFactory implements Factory<OrderCacheStoreAdapter> {

    @Override
    public OrderCacheStoreAdapter create() {
        return new OrderCacheStoreAdapter();
    }
}
