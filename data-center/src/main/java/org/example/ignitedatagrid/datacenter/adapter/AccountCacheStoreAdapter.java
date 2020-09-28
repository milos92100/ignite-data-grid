package org.example.ignitedatagrid.datacenter.adapter;


import org.example.ignitedatagrid.domain.entities.Account;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class AccountCacheStoreAdapter extends AbstractJdbcCacheStoreAdapter<Long, Account> {

    public AccountCacheStoreAdapter(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected PreparedStatement insertStatement(Connection connection, Account entity) throws SQLException {
        var statement = connection.prepareStatement("INSERT INTO [dbo].[Accounts] ([id], [name], [user_id]) VALUES (?, ?, ?)");
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getName());
        statement.setLong(3, entity.getUserId());
        return statement;
    }

    @Override
    protected PreparedStatement updateStatement(Connection connection, Account entity) throws SQLException {
        var statement = connection.prepareStatement("UPDATE [dbo].[Accounts] set [name] = ?, [user_id] = ? WHERE [id] = ?");
        statement.setString(1, entity.getName());
        statement.setLong(2, entity.getUserId());
        statement.setLong(3, entity.getId());
        return statement;
    }

    @Override
    protected PreparedStatement getAllStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM [dbo].[Accounts]");
    }

    @Override
    protected PreparedStatement getByKeyStatement(Connection connection, Long key) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM [dbo].[Accounts] WHERE [id] = ?");
        statement.setLong(1, key);
        return statement;
    }

    @Override
    protected PreparedStatement getAllByKeysStatement(Connection connection, Iterable<Long> keys) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM [dbo].[Accounts] WHERE id IN (?)");
        statement.setString(1, joinKeys(keys));
        return statement;
    }

    @Override
    protected PreparedStatement deleteStatement(Connection connection, Long key) throws SQLException {
        var statement = connection.prepareStatement("DELETE FROM [dbo].[Accounts] WHERE [id] = ?");
        statement.setLong(1, key);
        return statement;
    }

    @Override
    protected PreparedStatement deleteAllStatement(Connection connection, Collection<Long> keys) throws SQLException {
        var statement = connection.prepareStatement("DELETE FROM [dbo].[Accounts] WHERE [id] = IN (?)");
        statement.setString(1, joinKeys(keys));
        return statement;
    }

    @Override
    protected Long getKey(Account entity) {
        return entity.getId();
    }

    @Override
    protected Account readEntity(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getLong("user_id")
        );
    }

    @Override
    protected String getTableName() {
        return "Accounts";
    }
}
