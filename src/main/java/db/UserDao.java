package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final Connection c;

    UserDao(Connection c) {

        this.c = c;

    }

    public void initTable() throws SQLException {

        PreparedStatement p = c.prepareStatement("CREATE TABLE IF NOT EXISTS users(chatid varchar(256) PRIMARY KEY)");

        p.execute();

    }


    public void addNewUser(String chatId) throws SQLException {

        PreparedStatement p = c.prepareStatement("insert into users (chatid) values (?)");

        p.setString(1, chatId);

        p.execute();


    }

    public boolean checkUsersIsExist(String chatId) throws SQLException {


        PreparedStatement ps = c.prepareStatement("select * from users where chatid = ?");

        ps.setString(1,chatId);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        return rs.next();

    }

    public List<String> getUsersChatIds() throws SQLException {

        PreparedStatement p = c.prepareStatement("select chatid from users");

        p.execute();

        ResultSet rs = p.getResultSet();

        ArrayList<String> is = new ArrayList<>();

        while (rs.next()) {

            is.add(rs.getString("chatid"));

        }

        return is;

    }


}
