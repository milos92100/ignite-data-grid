package org.example.ignitedatagrid.datacenter.adapter;


import org.apache.ignite.IgniteLogger;
import org.apache.ignite.resources.LoggerResource;
import org.example.ignitedatagrid.domain.entities.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class OrderCacheStoreAdapter extends AbstractJdbcCacheStoreAdapter<Long, Order> {

    @LoggerResource(categoryClass = OrderCacheStoreAdapter.class, categoryName = "OrderCacheStoreAdapter")
    protected IgniteLogger LOGGER;


    @Override
    protected PreparedStatement insertStatement(Connection connection, Order entity) throws SQLException {
        var statement = connection.prepareStatement(
                "INSERT INTO [dbo].[Orders] ([id], [volume], [price], [account_id], [instrument_id]) VALUES (?, ?, ?, ?, ?)"
        );
        statement.setLong(1, entity.getId());
        statement.setInt(2, entity.getVolume());
        statement.setDouble(3, entity.getPrice());
        statement.setLong(4, entity.getAccountId());
        statement.setLong(5, entity.getInstrumentId());
        return statement;
    }


    @Override
    protected PreparedStatement updateStatement(Connection connection, Order entity) throws SQLException {
        var statement = connection.prepareStatement(
                "UPDATE [dbo].[Orders] SET [volume] = ?, [price] = ?, [account_id] = ?, [instrument_id] = ? WHERE [id] = ?"
        );
        statement.setInt(1, entity.getVolume());
        statement.setDouble(2, entity.getPrice());
        statement.setLong(3, entity.getAccountId());
        statement.setLong(4, entity.getInstrumentId());
        statement.setLong(5, entity.getId());
        return statement;
    }


    @Override
    protected PreparedStatement getAllStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM [dbo].[Orders]");
    }


    @Override
    protected PreparedStatement getByKeyStatement(Connection connection, Long key) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM [dbo].[Orders] WHERE [id] = ?");
        statement.setLong(1, key);
        return statement;
    }


    @Override
    protected PreparedStatement getAllByKeysStatement(Connection connection, Iterable<Long> keys) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM [dbo].[Orders] WHERE [id] IN (?)");
        statement.setString(1, joinKeys(keys));
        return statement;
    }

    @Override
    protected PreparedStatement deleteStatement(Connection connection, Long key) throws SQLException {
        var statement = connection.prepareStatement("DELETE FROM [dbo].[Orders] WHERE [id] = ?");
        statement.setLong(1, key);
        return statement;
    }

    @Override
    protected PreparedStatement deleteAllStatement(Connection connection, Collection<Long> keys) throws SQLException {
        var statement = connection.prepareStatement("DELETE FROM [dbo].[Orders] WHERE [id] IN (?)");
        statement.setString(1, joinKeys(keys));
        return statement;
    }

    @Override
    protected Long getKey(Order entity) {
        return entity.getId();
    }

    @Override
    protected Order readEntity(ResultSet resultSet) throws SQLException {
        return new Order(
                resultSet.getLong("id"),
                resultSet.getInt("volume"),
                resultSet.getDouble("price"),
                resultSet.getLong("account_id"),
                resultSet.getLong("instrument_id")
        );
    }

    @Override
    protected IgniteLogger getLogger() {
        return LOGGER;
    }
}
