package org.example.ignitedatagrid.datacenter.adapter;


import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.example.ignitedatagrid.domain.User;
import org.jetbrains.annotations.Nullable;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.Collection;
import java.util.Map;

public class UserCacheStoreAdapter extends CacheStoreAdapter<Long, User> {


    @Override
    public void loadCache(IgniteBiInClosure<Long, User> clo, @Nullable Object... args) throws CacheLoaderException {
        System.out.println("UserCacheStoreAdapter.loadCache");
    }

    @Override
    public void sessionEnd(boolean commit) throws CacheWriterException {
        System.out.println("UserCacheStoreAdapter.sessionEnd");
    }

    @Override
    public User load(Long aLong) throws CacheLoaderException {
        System.out.println("UserCacheStoreAdapter.load");
        return null;
    }

    @Override
    public Map<Long, User> loadAll(Iterable<? extends Long> iterable) throws CacheLoaderException {
        System.out.println("UserCacheStoreAdapter.loadAll");
        return null;
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends User> entry) throws CacheWriterException {
        System.out.println("UserCacheStoreAdapter.write");
    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends Long, ? extends User>> collection) throws CacheWriterException {
        System.out.println("UserCacheStoreAdapter.writeAll");
    }

    @Override
    public void delete(Object o) throws CacheWriterException {
        System.out.println("UserCacheStoreAdapter.delete");
    }

    @Override
    public void deleteAll(Collection<?> collection) throws CacheWriterException {
        System.out.println("UserCacheStoreAdapter.deleteAll");
    }
}
