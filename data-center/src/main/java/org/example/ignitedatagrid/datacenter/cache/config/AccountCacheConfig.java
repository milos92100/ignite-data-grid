package org.example.ignitedatagrid.datacenter.cache.config;

import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.configuration.CacheConfiguration;
import org.example.ignitedatagrid.datacenter.factory.AccountAdapterFactory;
import org.example.ignitedatagrid.domain.entities.Account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class AccountCacheConfig extends CacheConfiguration<Long, Account> {
    public static final String NAME = "AccountCache";

    public static AccountCacheConfig create() {
        var config = new AccountCacheConfig();
        var queryEntity = new QueryEntity();
        queryEntity.setKeyFieldName("id");
        queryEntity.setValueType(Account.class.getName());

        var fields = new LinkedHashMap<String, String>();
        fields.put("id", Long.class.getName());
        fields.put("name", String.class.getName());
        fields.put("user_id", Long.class.getName());
        queryEntity.setFields(fields);

        var indexes = new ArrayList<QueryIndex>(3);
        indexes.add(new QueryIndex("id"));
        indexes.add(new QueryIndex("name"));
        indexes.add(new QueryIndex("user_id"));
        queryEntity.setIndexes(indexes);

        config.setName(NAME);
        config.setQueryEntities(Collections.singletonList(queryEntity));
        config.setCacheStoreFactory(new AccountAdapterFactory());

        return config;
    }
}
