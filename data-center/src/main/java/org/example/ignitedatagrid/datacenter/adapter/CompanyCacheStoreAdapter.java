package org.example.ignitedatagrid.datacenter.adapter;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.example.ignitedatagrid.domain.Company;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.Collection;
import java.util.Map;

public class CompanyCacheStoreAdapter extends CacheStoreAdapter<Long, Company> {

    @Override
    public void loadCache(IgniteBiInClosure<Long, Company> clo, Object... args) {
        super.loadCache(clo, args);
    }


    @Override
    public Map<Long, Company> loadAll(Iterable<? extends Long> keys) {
        return super.loadAll(keys);
    }


    @Override
    public void writeAll(Collection<Cache.Entry<? extends Long, ? extends Company>> entries) {
        super.writeAll(entries);
    }


    @Override
    public void deleteAll(Collection<?> keys) {
        super.deleteAll(keys);
    }


    @Override
    public void sessionEnd(boolean commit) {
        super.sessionEnd(commit);
    }

    @Override
    public Company load(Long key) throws CacheLoaderException {
        return null;
    }


    @Override
    public void write(Cache.Entry<? extends Long, ? extends Company> entry) throws CacheWriterException {

    }


    @Override
    public void delete(Object key) throws CacheWriterException {

    }
}
