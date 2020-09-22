package org.example.ignitedatagrid.datacenter.cache.config;

import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.cache.QueryIndexType;

import org.apache.ignite.cache.affinity.AffinityFunction;
import org.apache.ignite.cache.affinity.AffinityFunctionContext;
import org.apache.ignite.cache.affinity.AffinityKeyMapper;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.example.ignitedatagrid.datacenter.cache.key.UserKey;
import org.example.ignitedatagrid.datacenter.factory.UserAdapterFactory;
import org.example.ignitedatagrid.domain.User;

import java.util.*;

public class UserCacheConfig extends CacheConfiguration<Long, User> {
    private static final String NAME = "UserCache";

    public static final UserCacheConfig INSTANCE = new UserCacheConfig() {{
        var queryEntity = new QueryEntity();
        queryEntity.setKeyType(UserKey.class.getName());
        queryEntity.setValueType(User.class.getName());

        var fields = new LinkedHashMap<String, String>();
        fields.put("id", Long.class.getName());
        fields.put("firstName", String.class.getName());
        fields.put("lastName", String.class.getName());
        fields.put("addresses", Set.class.getName());
        queryEntity.setFields(fields);

        var indexes = new ArrayList<QueryIndex>(2);
        indexes.add(new QueryIndex("id"));
        indexes.add(new QueryIndex("firstName"));
        indexes.add(new QueryIndex(List.of("firstName", "lastName"), QueryIndexType.SORTED));
        queryEntity.setIndexes(indexes);

        setName(NAME);
        setQueryEntities(Collections.singletonList(queryEntity));
        setCacheStoreFactory(new UserAdapterFactory());

    }};
}
