package org.example.ignitedatagrid.datacenter.adapter;


import org.example.ignitedatagrid.domain.entities.User;

import javax.cache.Cache;
import javax.cache.integration.CacheWriterException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class UserCacheStoreAdapter extends AbstractJdbcCacheStoreAdapter<Long, User> {

    private static final String TABLE_NAME = "Users";
    private static final String INSERT_QUERY = "INSERT INTO Users (id, first_name, last_name) VALUES (?, ?, ?)";

    public UserCacheStoreAdapter(DataSource dataSource) {
        super(dataSource);
    }

    private void writeInternal(Long id, User user) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(INSERT_QUERY)) {

            statement.setLong(1, id);
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());

            var affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting user failed, no rows affected.");
            }
        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheWriterException(e);
        }
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends User> entry) throws CacheWriterException {
        writeInternal(entry.getKey(), entry.getValue());
    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends Long, ? extends User>> collection) throws CacheWriterException {
        collection.forEach(entry -> writeInternal(entry.getKey(), entry.getValue()));
    }

    @Override
    protected Long getKey(User entity) {
        return entity.getId();
    }

    @Override
    protected User readEntity(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong("id"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName")
        );
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }
}
