package com.progresssoft;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;

public class DealRepository {

    private static final Logger logger = LoggerFactory.getLogger(DealRepository.class);
    private final HikariDataSource dataSource;

    public DealRepository() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/fx_deal_db");
        config.setUsername("user");
        config.setPassword("password");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    public void saveDeal(Deal deal) throws SQLException {
        String sql = "INSERT INTO fx_deals (deal_id, from_currency_iso_code, to_currency_iso_code, deal_timestamp, deal_amount) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, deal.getDealId());
            statement.setString(2, deal.getFromCurrencyIsoCode());
            statement.setString(3, deal.getToCurrencyIsoCode());
            statement.setTimestamp(4, Timestamp.valueOf(deal.getDealTimestamp()));
            statement.setBigDecimal(5, deal.getDealAmount());
            statement.executeUpdate();
            logger.info("Deal with ID {} saved successfully.", deal.getDealId());
        } catch (SQLException e) {
            logger.error("Error saving deal with ID {}: {}", deal.getDealId(), e.getMessage());
            throw e;
        }
    }

    public boolean dealExists(UUID dealId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM fx_deals WHERE deal_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, dealId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking for existence of deal with ID {}: {}", dealId, e.getMessage());
            throw e;
        }
        return false;
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
