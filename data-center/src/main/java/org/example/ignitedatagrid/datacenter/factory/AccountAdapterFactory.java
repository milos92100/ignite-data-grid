package org.example.ignitedatagrid.datacenter.factory;

import org.example.ignitedatagrid.datacenter.adapter.AccountCacheStoreAdapter;

import javax.cache.configuration.Factory;

public class AccountAdapterFactory implements Factory<AccountCacheStoreAdapter> {

    @Override
    public AccountCacheStoreAdapter create() {
        return new AccountCacheStoreAdapter();
    }
}
