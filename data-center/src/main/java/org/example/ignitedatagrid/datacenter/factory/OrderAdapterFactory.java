package org.example.ignitedatagrid.datacenter.factory;

import org.example.ignitedatagrid.datacenter.adapter.OrderCacheStoreAdapter;

import javax.sql.DataSource;

public class OrderAdapterFactory extends AbstractAdapterFactory<OrderCacheStoreAdapter> {

    public OrderAdapterFactory(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public OrderCacheStoreAdapter create() {
        return new OrderCacheStoreAdapter(dataSource);
    }
}
