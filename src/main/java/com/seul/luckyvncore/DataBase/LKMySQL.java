package com.seul.luckyvncore.DataBase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LKMySQL {

    public HikariDataSource ds;
    private boolean isConnected = false;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public boolean isConnected() {
        try (Connection connection = ds.getConnection()) {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Map<String, String> mysqluser;
    public LKMySQL(ConfigurationSection mysqlconfig) {

        this.mysqluser = new HashMap<>();
        for (String key : mysqlconfig.getKeys(false)) {
            mysqluser.put(key, mysqlconfig.getString(key));
        }


        try {
            connect();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void connect() throws ClassNotFoundException, SQLException {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setTransactionIsolation("TRANSACTION_READ_COMMITTED");

        config.setPoolName("BedwarsEssentials-Pool");
        config.setJdbcUrl("jdbc:mariadb://" + mysqluser.get("host") + ":" + mysqluser.get("port") + "/" + mysqluser.get("database") + "?autoReconnect=true&useSSL=false");
        config.setUsername(mysqluser.get("user"));
        config.setPassword(mysqluser.get("pass"));

        config.setMaxLifetime(Long.parseLong(mysqluser.get("maxLifetime")) * 1000L);
        config.setMaximumPoolSize(Integer.parseInt(mysqluser.get("poolSize")));

        // HikariCP configuration
        config.addDataSourceProperty("useSSL", mysqluser.get("ssl"));
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("encoding", "UTF-8");
        config.addDataSourceProperty("useUnicode", "true");

        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("jdbcCompliantTruncation", "false");

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "275");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
        scheduler.scheduleAtFixedRate(this::checkConnection, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void checkConnection() {
        try (Connection connection = ds.getConnection()) {
            if (connection.isValid(1000)) { // Kiểm tra tính hợp lệ của kết nối
                isConnected = true;
            } else {
                isConnected = false;
                reconnect(); // Gọi hàm reconnect() để thử kết nối lại
            }
        } catch (SQLException e) {
            isConnected = false;
            reconnect();
        }
    }

    private void reconnect() {
        try {
            // Thử kết nối lại
            if (!ds.isClosed()){
                ds.close(); // Đóng kết nối hiện tại (nếu có)
            }
            connect(); // Thử kết nối lại
            System.out.println("RECONNECT");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (ds != null) {
            try {
                if (!ds.isClosed()) {
                    ds.close();
                }
            } finally {
                ds = null;
            }
        }
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
