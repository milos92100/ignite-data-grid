package org.example.ignitedatagrid.datacenter.adapter;


import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.LoggerResource;
import org.example.ignitedatagrid.domain.entities.Instrument;
import org.jetbrains.annotations.Nullable;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class InstrumentCacheStoreAdapter extends CacheStoreAdapter<Long, Instrument> {

    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM [dbo].[Users]";
    private static final String GET_USER_BY_ID = "SELECT * FROM [dbo].[Users] where [id] = ?";

    @LoggerResource
    private IgniteLogger LOGGER;

    private final DataSource dataSource;

    public InstrumentCacheStoreAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Instrument toInstrument(ResultSet cursor) throws SQLException {
        return new Instrument(
                cursor.getLong("id"),
                cursor.getString("name"),
                cursor.getString("market")
        );
    }

    @Override
    public void loadCache(IgniteBiInClosure<Long, Instrument> clo, @Nullable Object... args) throws CacheLoaderException {
        try (var connection = dataSource.getConnection(); //
             var statement = connection.prepareStatement(GET_ALL_USERS_QUERY);
             var cursor = statement.executeQuery()) {

            long cnt = 0L;
            while (cursor.next()) {
                var user = toInstrument(cursor);
                clo.apply(user.getId(), user);
                cnt++;
            }

            LOGGER.info("Loaded " + cnt + " users");
        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }
    }


    @Override
    public Instrument load(Long aLong) throws CacheLoaderException {
//        try (var connection = dataSource.getConnection()) {
//
//            var statement = connection.prepareStatement(GET_USER_BY_ID);
//            statement.setLong(1, aLong);
//
//            var cursor = statement.executeQuery();
//
//            if (cursor.next()) {
//                var user = toUser(cursor);
//                LOGGER.debug("Loaded user: " + user.getId());
//                return user;
//            }
//
//        } catch (SQLException e) {
//            LOGGER.error(e.getSQLState(), e);
//            throw new CacheLoaderException(e);
//        }
        return null;

    }

    @Override
    public Map<Long, Instrument> loadAll(Iterable<? extends Long> iterable) throws CacheLoaderException {
        System.out.println("UserCacheStoreAdapter.loadAll");
        return null;
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Instrument> entry) throws CacheWriterException {
        System.out.println("UserCacheStoreAdapter.write");
    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends Long, ? extends Instrument>> collection) throws CacheWriterException {
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
