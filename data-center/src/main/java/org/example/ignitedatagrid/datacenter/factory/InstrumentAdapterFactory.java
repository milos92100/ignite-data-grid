package org.example.ignitedatagrid.datacenter.factory;

import org.example.ignitedatagrid.datacenter.adapter.InstrumentCacheStoreAdapter;

import javax.sql.DataSource;

public class InstrumentAdapterFactory extends AbstractAdapterFactory<InstrumentCacheStoreAdapter> {

    public InstrumentAdapterFactory(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public InstrumentCacheStoreAdapter create() {
        return new InstrumentCacheStoreAdapter(dataSource);
    }
}
