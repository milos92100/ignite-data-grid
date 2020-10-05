package org.example.ignitedatagrid.datacenter.factory;

import org.example.ignitedatagrid.datacenter.adapter.UserCacheStoreAdapter;

import javax.cache.configuration.Factory;

public class UserAdapterFactory implements Factory<UserCacheStoreAdapter> {

    @Override
    public UserCacheStoreAdapter create() {
        return new UserCacheStoreAdapter();
    }
}
