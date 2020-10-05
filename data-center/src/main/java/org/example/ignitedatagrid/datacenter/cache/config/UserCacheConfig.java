package org.example.ignitedatagrid.datacenter.cache.config;

import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.cache.QueryIndexType;
import org.apache.ignite.configuration.CacheConfiguration;
import org.example.ignitedatagrid.datacenter.factory.UserAdapterFactory;
import org.example.ignitedatagrid.domain.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class UserCacheConfig extends CacheConfiguration<Long, User> {
    public static final String NAME = "UserCache";

    public static UserCacheConfig create() {
        var config = new UserCacheConfig();
        var queryEntity = new QueryEntity();
        queryEntity.setKeyFieldName("id");
        queryEntity.setValueType(User.class.getName());

        var fields = new LinkedHashMap<String, String>();
        fields.put("id", Long.class.getName());
        fields.put("firstName", String.class.getName());
        fields.put("lastName", String.class.getName());
        queryEntity.setFields(fields);

        var indexes = new ArrayList<QueryIndex>(3);
        indexes.add(new QueryIndex("id"));
        indexes.add(new QueryIndex("firstName"));
        indexes.add(new QueryIndex(List.of("firstName", "lastName"), QueryIndexType.SORTED));
        queryEntity.setIndexes(indexes);

        config.setName(NAME);
        config.setQueryEntities(Collections.singletonList(queryEntity));
        config.setCacheStoreFactory(new UserAdapterFactory());

        return config;
    }
}
