package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {

    private static final int CONNECTION_NUMBER = 10;
    private static final String JDBC_URL = "jdbc:h2:~/bp";

    private static final String DB_LOGIN = "bp";
    private static final String DB_PASSWORD = "bp";

    private static BlockingQueue<Connection> connections = new LinkedBlockingQueue<>();

    static {

        try {

            Class.forName("org.h2.Driver");

        } catch (ClassNotFoundException e) {

            e.printStackTrace();

        }

        Connection connection;
        try {
            for (int i = 0; i < CONNECTION_NUMBER; i++) {
                connection = DriverManager.getConnection(JDBC_URL, DB_LOGIN, DB_PASSWORD);
                connections.add(connection);
            }
        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    private ConnectionPool() {
    }

    static Connection connection;

    public synchronized static Connection getConnection() {

        try {

            connection = connections.poll(5, TimeUnit.SECONDS);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        return connection;

    }

    public synchronized static void releaseConnection(Connection connection) {
        try {

            connections.offer(connection, 5, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
