package org.example.ignitedatagrid.datacenter.adapter;

import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.SpringResource;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractJdbcCacheStoreAdapter<K, V> extends CacheStoreAdapter<K, V> {

    @SpringResource(resourceName = "dataSource")
    protected DataSource dataSource;

    @Override
    public void loadCache(IgniteBiInClosure<K, V> closure, Object... args) {
        try (var connection = dataSource.getConnection();
             var statement = getAllStatement(connection)) {

            getLogger().info("Executing: " + statement);

            try (var resultSet = statement.executeQuery()) {
                long cnt = 0L;
                while (resultSet.next()) {
                    var entity = readEntity(resultSet);
                    closure.apply(getKey(entity), entity);
                    cnt++;
                }

                getLogger().info("Loaded " + cnt + "  into cache.");
            }
        } catch (SQLException e) {
            getLogger().error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }
    }

    private void writeInternal(K id, V entity) {
        try (var connection = dataSource.getConnection();
             var statement = insertStatement(connection, entity)) {

            getLogger().debug("Write: " + entity);

            statement.executeUpdate();

        } catch (SQLException e) {
            getLogger().error(e.getSQLState(), e);
            throw new CacheWriterException(e);
        }
    }

    @Override
    public void write(Cache.Entry<? extends K, ? extends V> entry) throws CacheWriterException {
        writeInternal(entry.getKey(), entry.getValue());
    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends K, ? extends V>> collection) throws CacheWriterException {
        getLogger().info("writeAll " + collection.size());
        collection.forEach(entry -> writeInternal(entry.getKey(), entry.getValue()));
    }

    @Override
    public V load(K key) throws CacheLoaderException {
        if (key == null) return null;

        try (var connection = dataSource.getConnection();
             var statement = getByKeyStatement(connection, key)) {

            getLogger().info("Executing: " + statement);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    V entity = readEntity(resultSet);
                    getLogger().info("Loaded : " + getKey(entity));
                    return entity;
                }
            }
        } catch (SQLException e) {
            getLogger().error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }
        return null;
    }


    @Override
    public Map<K, V> loadAll(Iterable<? extends K> keys) {
        if (!keys.iterator().hasNext()) new HashMap<>();

        Map<K, V> result = new HashMap<>();

        try (var connection = dataSource.getConnection();
             var statement = getAllByKeysStatement(connection, (Iterable<K>) keys)) {

            getLogger().info("Executing: " + statement);

            try (var resultSet = statement.executeQuery()) {
                long cnt = 0L;
                while (resultSet.next()) {
                    var entity = readEntity(resultSet);
                    result.put(getKey(entity), entity);
                    cnt++;
                }

                getLogger().info("Loaded " + cnt + " records.");
            }
        } catch (SQLException e) {
            getLogger().error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }

        return result;
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        if (key == null) return;

        try (var connection = dataSource.getConnection();
             var statement = deleteStatement(connection, (K) key)) {

            getLogger().info("Executing: " + statement);

            var affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting key: " + key + " failed, no rows affected.");
            }
        } catch (SQLException e) {
            getLogger().error(e.getSQLState(), e);
            throw new CacheWriterException(e);
        }
    }

    @Override
    public void deleteAll(Collection<?> collection) throws CacheWriterException {
        if (collection.isEmpty()) return;

        try (var connection = dataSource.getConnection();
             var statement = deleteAllStatement(connection, (Collection<K>) collection)) {

            getLogger().info("Executing: " + statement);

            var affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting failed, no rows affected.");
            }

            getLogger().info("Deleted " + affectedRows + " rows");
        } catch (SQLException e) {
            getLogger().error(e.getSQLState(), e);
            throw new CacheWriterException(e);
        }
    }

    protected <E> String joinKeys(Iterable<K> keys) {
        return StreamSupport.stream(keys.spliterator(), false)
                .map(Objects::toString)
                .collect(Collectors.joining(","));
    }

    /**
     * Create an prepared statement for executing an entity insert
     *
     * @param connection Connection to data source
     * @param entity     Entity to insert
     * @return PreparedStatement Insert statement
     * @throws SQLException if creation of statement fails
     */
    protected abstract PreparedStatement insertStatement(Connection connection, V entity) throws SQLException;

    /**
     * Creates an prepared statement for executing and entity update
     *
     * @param connection Connection to data source
     * @param entity     Entity to update
     * @return PreparedStatement Update statement
     * @throws SQLException if creation of statement fails
     */
    protected abstract PreparedStatement updateStatement(Connection connection, V entity) throws SQLException;

    /**
     * @param connection
     * @return
     * @throws SQLException
     */
    protected abstract PreparedStatement getAllStatement(Connection connection) throws SQLException;

    /**
     * @param connection
     * @param entity
     * @return
     * @throws SQLException
     */
    protected abstract PreparedStatement getByKeyStatement(Connection connection, K entity) throws SQLException;

    /**
     * @param connection
     * @param keys
     * @return
     * @throws SQLException
     */
    protected abstract PreparedStatement getAllByKeysStatement(Connection connection, Iterable<K> keys) throws SQLException;

    protected abstract PreparedStatement deleteStatement(Connection connection, K key) throws SQLException;

    protected abstract PreparedStatement deleteAllStatement(Connection connection, Collection<K> keys) throws SQLException;

    protected abstract K getKey(V entity);

    protected abstract V readEntity(ResultSet resultSet) throws SQLException;

    protected abstract IgniteLogger getLogger();

}
