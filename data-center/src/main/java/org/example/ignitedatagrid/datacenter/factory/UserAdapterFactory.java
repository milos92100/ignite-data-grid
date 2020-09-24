package org.example.ignitedatagrid.datacenter.factory;

import org.example.ignitedatagrid.datacenter.adapter.UserCacheStoreAdapter;

import javax.sql.DataSource;

public class UserAdapterFactory extends AbstractAdapterFactory<UserCacheStoreAdapter> {

    public UserAdapterFactory(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public UserCacheStoreAdapter create() {
        return new UserCacheStoreAdapter(dataSource);
    }
}
