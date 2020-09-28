package org.example.ignitedatagrid.datacenter.adapter;


import org.example.ignitedatagrid.domain.entities.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class UserCacheStoreAdapter extends AbstractJdbcCacheStoreAdapter<Long, User> {

    public UserCacheStoreAdapter(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected PreparedStatement insertStatement(Connection connection, User entity) throws SQLException {
        var statement = connection.prepareStatement("INSERT INTO [dbo].[Users] ([id], [first_name], [last_name]) VALUES (?, ?, ?)");
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getFirstName());
        statement.setString(3, entity.getLastName());
        return statement;
    }

    @Override
    protected PreparedStatement updateStatement(Connection connection, User entity) throws SQLException {
        var statement = connection.prepareStatement("UPDATE [dbo].[Users] set [first_name] = ?, [last_name] = ? WHERE [id] = ?");
        statement.setString(1, entity.getFirstName());
        statement.setString(2, entity.getLastName());
        statement.setLong(3, entity.getId());
        return statement;
    }

    @Override
    protected PreparedStatement getAllStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM [dbo].[Users]");
    }

    @Override
    protected PreparedStatement getByKeyStatement(Connection connection, Long key) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM [dbo].[Users] WHERE id = ?");
        statement.setLong(1, key);
        return statement;
    }

    @Override
    protected PreparedStatement getAllByKeysStatement(Connection connection, Iterable<Long> keys) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM [dbo].[Users] WHERE id IN (?)");
        statement.setString(1, joinKeys(keys));
        return statement;
    }

    @Override
    protected PreparedStatement deleteStatement(Connection connection, Long key) throws SQLException {
        var statement = connection.prepareStatement("DELETE FROM [dbo].[Users] WHERE [id] = ?");
        statement.setLong(1, key);
        return statement;
    }

    @Override
    protected PreparedStatement deleteAllStatement(Connection connection, Collection<Long> keys) throws SQLException {
        var statement = connection.prepareStatement("DELETE FROM [dbo].[Users] WHERE [id] IN (?)");
        statement.setString(1, joinKeys(keys));
        return statement;
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
        return "[dbo].[Users]";
    }
}
