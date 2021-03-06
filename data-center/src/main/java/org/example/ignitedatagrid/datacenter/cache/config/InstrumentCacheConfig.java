package org.example.ignitedatagrid.datacenter.cache.config;

import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.configuration.CacheConfiguration;
import org.example.ignitedatagrid.datacenter.factory.InstrumentAdapterFactory;
import org.example.ignitedatagrid.domain.entities.Instrument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class InstrumentCacheConfig extends CacheConfiguration<Long, Instrument> {
    public static final String NAME = "InstrumentCache";

    public static InstrumentCacheConfig create() {
        var config = new InstrumentCacheConfig();
        var queryEntity = new QueryEntity();
        queryEntity.setKeyFieldName("id");
        queryEntity.setValueType(Instrument.class.getName());

        var fields = new LinkedHashMap<String, String>();
        fields.put("id", Long.class.getName());
        fields.put("name", String.class.getName());
        fields.put("market", String.class.getName());
        queryEntity.setFields(fields);

        var indexes = new ArrayList<QueryIndex>(3);
        indexes.add(new QueryIndex("id"));
        indexes.add(new QueryIndex("name"));
        indexes.add(new QueryIndex("market"));
        queryEntity.setIndexes(indexes);

        config.setName(NAME);
        config.setQueryEntities(Collections.singletonList(queryEntity));
        config.setCacheStoreFactory(new InstrumentAdapterFactory());

        return config;
    }
}
