package db;

import java.sql.Connection;

public class DaoFactory {

    private static Connection connection = ConnectionPool.getConnection();

    private static DaoFactory daoFactory = new DaoFactory();

    public static DaoFactory getFactory() {

        return daoFactory;

    }

    public ADDao getADAO(){

        return new ADDao(connection);

    }

    public UserDao getUserDao(){

        return new UserDao(connection);

    }

}
