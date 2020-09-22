package org.example.ignitedatagrid.datacenter.cache.config;

import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.configuration.CacheConfiguration;
import org.example.ignitedatagrid.datacenter.factory.CompanyAdapterFactory;
import org.example.ignitedatagrid.domain.Address;
import org.example.ignitedatagrid.domain.Company;
import org.example.ignitedatagrid.domain.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class CompanyCacheConfig extends CacheConfiguration<Long, Company> {
    private static final String NAME = "CompanyCache";

    public static final CompanyCacheConfig INSTANCE = new CompanyCacheConfig() {{
        var queryEntity = new QueryEntity();
        queryEntity.setKeyType(Long.class.getName());
        queryEntity.setValueType(Company.class.getName());

        var fields = new LinkedHashMap<String, String>();
        fields.put("id", Long.class.getName());
        fields.put("name", String.class.getName());
        fields.put("taxCode", String.class.getName());
        fields.put("primaryAddress", Address.class.getName());
        fields.put("responsiblePerson", User.class.getName());
        queryEntity.setFields(fields);

        var indexes = new ArrayList<QueryIndex>(2);
        indexes.add(new QueryIndex("id"));
        indexes.add(new QueryIndex("name"));
        indexes.add(new QueryIndex("taxCode"));
        queryEntity.setIndexes(indexes);

        setName(NAME);
        setQueryEntities(Collections.singletonList(queryEntity));
        setCacheStoreFactory(new CompanyAdapterFactory());
    }};
}
