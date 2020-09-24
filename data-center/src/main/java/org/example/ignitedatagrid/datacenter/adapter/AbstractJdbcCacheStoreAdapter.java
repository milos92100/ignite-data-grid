package org.example.ignitedatagrid.datacenter.adapter;

import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.LoggerResource;

import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractJdbcCacheStoreAdapter<K, V> extends CacheStoreAdapter<K, V> {

    private static final String GET_ALL = "SELECT * FROM %s";
    private static final String GET_BY_ID = "SELECT * FROM %s WHERE id = ?";
    private static final String GET_ALL_BY_IDS = "SELECT * FROM %s WHERE id IN(?)";
    private static final String DELETE = "DELETE FROM %s WHERE id = ?";
    private static final String DELETE_ALL = "DELETE FROM %s WHERE id = IN(?)";

    @LoggerResource
    protected IgniteLogger LOGGER;

    protected final DataSource dataSource;

    protected AbstractJdbcCacheStoreAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void loadCache(IgniteBiInClosure<K, V> closure, Object... args) {
        try (var connection = dataSource.getConnection(); //
             var statement = connection.prepareStatement(String.format(GET_ALL, getTableName()));
             var cursor = statement.executeQuery()) {

            long cnt = 0L;
            while (cursor.next()) {
                var entity = readEntity(cursor);
                closure.apply(getKey(entity), entity);
                cnt++;
            }

            LOGGER.info("Loaded " + cnt + " from " + getTableName());
        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }
    }

    @Override
    public V load(K key) throws CacheLoaderException {
        if (key == null) return null;

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(String.format(GET_BY_ID, getTableName()))) {

            statement.setObject(1, key);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    V entity = readEntity(resultSet);
                    LOGGER.debug("Loaded : " + getKey(entity));
                    return entity;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }
        return null;
    }


    @Override
    public Map<K, V> loadAll(Iterable<? extends K> keys) {
        if (!keys.iterator().hasNext()) new HashMap<>();

        Map<K, V> result = new HashMap<>();

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(String.format(GET_ALL_BY_IDS, getTableName()))) {

            var ids = StreamSupport.stream(keys.spliterator(), false)
                    .map(Objects::toString)
                    .collect(Collectors.joining(","));

            statement.setString(1, ids);

            try (var resultSet = statement.executeQuery()) {
                long cnt = 0L;
                while (resultSet.next()) {
                    var entity = readEntity(resultSet);
                    result.put(getKey(entity), entity);
                    cnt++;
                }

                LOGGER.info("Loaded " + cnt + " from " + getTableName());
            }
        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }

        return result;
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        if (key == null) return;

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(String.format(DELETE, getTableName()))) {

            statement.setLong(1, (Long) key);

            var affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting key: " + key + " from: " + getTableName() + " failed, no rows affected.");
            }
        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheWriterException(e);
        }
    }

    @Override
    public void deleteAll(Collection<?> collection) throws CacheWriterException {
        if (collection.isEmpty()) return;

        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(String.format(DELETE_ALL, getTableName()))) {

            var ids = collection.stream()
                    .map(Objects::toString)
                    .collect(Collectors.joining(","));

            statement.setString(1, ids);

            var affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting from:" + getTableName() + " failed, no rows affected.");
            }
        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheWriterException(e);
        }
    }

    protected abstract K getKey(V entity);

    protected abstract V readEntity(ResultSet resultSet) throws SQLException;

    protected abstract String getTableName();

}
