package org.example.ignitedatagrid.datacenter.factory;

import org.example.ignitedatagrid.datacenter.adapter.UserCacheStoreAdapter;

import javax.cache.configuration.Factory;
import javax.sql.DataSource;

public abstract class AbstractAdapterFactory<T> implements Factory<T> {
    protected final DataSource dataSource;

    public AbstractAdapterFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
