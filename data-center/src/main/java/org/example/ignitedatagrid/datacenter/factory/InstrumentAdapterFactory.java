package org.example.ignitedatagrid.datacenter.factory;

import org.example.ignitedatagrid.datacenter.adapter.InstrumentCacheStoreAdapter;

import javax.cache.configuration.Factory;

public class InstrumentAdapterFactory implements Factory<InstrumentCacheStoreAdapter> {

    @Override
    public InstrumentCacheStoreAdapter create() {
        return new InstrumentCacheStoreAdapter();
    }
}
