package org.example.ignitedatagrid.datacenter.adapter;


import org.apache.ignite.IgniteLogger;
import org.apache.ignite.resources.LoggerResource;
import org.example.ignitedatagrid.domain.entities.Instrument;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class InstrumentCacheStoreAdapter extends AbstractJdbcCacheStoreAdapter<Long, Instrument> {

    @LoggerResource(categoryClass = InstrumentCacheStoreAdapter.class, categoryName = "InstrumentCacheStoreAdapter")
    protected IgniteLogger LOGGER;

    @Override
    protected PreparedStatement insertStatement(Connection connection, Instrument entity) throws SQLException {
        var statement = connection.prepareStatement("INSERT INTO [dbo].[Instruments] ([id], [name], [market]) VALUES (?, ?, ?)");
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getName());
        statement.setString(3, entity.getMarket());
        return statement;
    }

    @Override
    protected PreparedStatement updateStatement(Connection connection, Instrument entity) throws SQLException {
        var statement = connection.prepareStatement("UPDATE [dbo].[Instruments] set [name] = ?, [market] = ? WHERE [id] = ?");
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getMarket());
        statement.setLong(3, entity.getId());
        return statement;
    }

    @Override
    protected PreparedStatement getAllStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM [dbo].[Instruments]");
    }

    @Override
    protected PreparedStatement getByKeyStatement(Connection connection, Long key) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM [dbo].[Instruments] WHERE [id] = ?");
        statement.setLong(1, key);
        return statement;
    }

    @Override
    protected PreparedStatement getAllByKeysStatement(Connection connection, Iterable<Long> keys) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM [dbo].[Instruments] WHERE [id] IN (?)");
        statement.setString(1, joinKeys(keys));
        return statement;
    }

    @Override
    protected PreparedStatement deleteStatement(Connection connection, Long key) throws SQLException {
        var statement = connection.prepareStatement("DELETE FROM [dbo].[Instruments] WHERE [id] = ?");
        statement.setLong(1, key);
        return statement;
    }

    @Override
    protected PreparedStatement deleteAllStatement(Connection connection, Collection<Long> keys) throws SQLException {
        var statement = connection.prepareStatement("DELETE FROM [dbo].[Instruments] WHERE [id] IN (?)");
        statement.setString(1, joinKeys(keys));
        return statement;
    }

    @Override
    protected Long getKey(Instrument entity) {
        return entity.getId();
    }

    @Override
    protected Instrument readEntity(ResultSet resultSet) throws SQLException {
        return new Instrument(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("market")
        );
    }

    @Override
    protected IgniteLogger getLogger() {
        return LOGGER;
    }
}
