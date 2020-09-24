package org.example.ignitedatagrid.datacenter.adapter;


import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.LoggerResource;
import org.example.ignitedatagrid.domain.entities.Account;
import org.jetbrains.annotations.Nullable;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class AccountCacheStoreAdapter extends CacheStoreAdapter<Long, Account> {

    private static final String GET_ALL_ACCOUNTS_QUERY = "SELECT * FROM [dbo].[Accounts]";
    private static final String GET_ACCOUNT_BY_ID = "SELECT * FROM [dbo].[Accounts] WHERE [id] = ?";

    @LoggerResource
    private IgniteLogger LOGGER;

    private final DataSource dataSource;

    public AccountCacheStoreAdapter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Account toAccount(ResultSet cursor) throws SQLException {
        return new Account(
                cursor.getLong("id"),
                cursor.getString("name"),
                cursor.getLong("user_id")
        );
    }

    @Override
    public void loadCache(IgniteBiInClosure<Long, Account> clo, @Nullable Object... args) throws CacheLoaderException {
        try (var connection = dataSource.getConnection(); //
             var statement = connection.prepareStatement(GET_ALL_ACCOUNTS_QUERY);
             var cursor = statement.executeQuery()) {

            long cnt = 0L;
            while (cursor.next()) {
                var account = toAccount(cursor);
                clo.apply(account.getId(), account);
                cnt++;
            }

            LOGGER.info("Loaded " + cnt + " accounts");
        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }
    }


    @Override
    public Account load(Long aLong) throws CacheLoaderException {
        try (var connection = dataSource.getConnection()) {

            var statement = connection.prepareStatement(GET_ACCOUNT_BY_ID);
            statement.setLong(1, aLong);

            var cursor = statement.executeQuery();

            if (cursor.next()) {
                var account = toAccount(cursor);
                LOGGER.debug("Loaded account: " + account.getId());
                return account;
            }

        } catch (SQLException e) {
            LOGGER.error(e.getSQLState(), e);
            throw new CacheLoaderException(e);
        }
        return null;
    }

    @Override
    public Map<Long, Account> loadAll(Iterable<? extends Long> iterable) throws CacheLoaderException {
        System.out.println("UserCacheStoreAdapter.loadAll");
        return null;
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Account> entry) throws CacheWriterException {
        System.out.println("UserCacheStoreAdapter.write");
    }

    @Override
    public void writeAll(Collection<Cache.Entry<? extends Long, ? extends Account>> collection) throws CacheWriterException {
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
