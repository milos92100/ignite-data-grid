package org.example.ignitedatagrid.datacenter.factory;

import org.example.ignitedatagrid.datacenter.adapter.AccountCacheStoreAdapter;

import javax.sql.DataSource;

public class AccountAdapterFactory extends AbstractAdapterFactory<AccountCacheStoreAdapter> {

    public AccountAdapterFactory(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public AccountCacheStoreAdapter create() {
        return new AccountCacheStoreAdapter(dataSource);
    }
}
