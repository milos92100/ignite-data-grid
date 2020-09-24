package org.example.ignitedatagrid.datacenter.adapter;


import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.LoggerResource;
import org.example.ignitedatagrid.domain.entities.Order;
import org.jetbrains.annotations.Nullable;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class OrderCacheStoreAdapter extends CacheStoreAdapter<Long, Order> {
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM [dbo].[Orders]";
    private static final String GET_USER_BY_ID = "SELECT * FROM [dbo].[Orders] where [id] = ?";

    @LoggerResource
    private IgniteLogger LOGGER;

    private final DataSource dataSource;

    public OrderCacheStoreAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Order toOrder(ResultSet cursor) throws SQLException {
        return new Order(
                cursor.getLong("id"),
                cursor.getInt("volume"),
                cursor.getDouble("price"),
                cursor.getLong("account_id"),
                cursor.getLong("instrument_id")
        );
    }

    @Override
    public void loadCache(IgniteBiInClosure<Long, Order> clo, @Nullable Object... args) throws CacheLoaderException {
        try (var connection = dataSource.getConnection(); //
             var statement = connection.prepareStatement(GET_ALL_USERS_QUERY);
             var cursor = statement.executeQuery()) {

            long cnt = 0L;
            while (cursor.next()) {
                var account = toOrder(cursor);
                clo.apply(account.getId(), account);
                cnt++;
            }

            LOGGER.info("Loaded " + cnt + " orders");
        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }
    }


    @Override
    public Order load(Long aLong) throws CacheLoaderException {
        try (var connection = dataSource.getConnection()) {

            var statement = connection.prepareStatement(GET_USER_BY_ID);
            statement.setLong(1, aLong);

            var cursor = statement.executeQuery();

            if (cursor.next()) {
                var order = toOrder(cursor);
                LOGGER.debug("Loaded order: " + order.getId());
                return order;
            }

        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }
        return null;
    }

    @Override
    public Map<Long, Order> loadAll(Iterable<? extends Long> iterable) throws CacheLoaderException {
        System.out.println("UserCacheStoreAdapter.loadAll");
        return null;
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Order> entry) throws CacheWriterException {
        System.out.println("UserCacheStoreAdapter.write");
    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends Long, ? extends Order>> collection) throws CacheWriterException {
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
