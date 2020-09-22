package org.example.ignitedatagrid.datacenter.factory;

import org.example.ignitedatagrid.datacenter.adapter.CompanyCacheStoreAdapter;

import javax.cache.configuration.Factory;

public class CompanyAdapterFactory implements Factory<CompanyCacheStoreAdapter> {

    @Override
    public CompanyCacheStoreAdapter create() {
        return new CompanyCacheStoreAdapter();
    }
}
