package db;

import model.Ad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ADDao {

    private final Connection c;

    public ADDao(Connection c) {

        this.c = c;

    }

    public void initTable() throws SQLException {

        PreparedStatement p = c.prepareStatement("CREATE TABLE IF NOT EXISTS ad(id bigint auto_increment PRIMARY KEY," +
                "header TEXT, pub_date TEXT, ad_text TEXT)");

        p.execute();

    }

    public void addAd(Ad ad) throws SQLException {

        PreparedStatement ps = c.prepareStatement("Insert into ad (header, pub_date, ad_text) values (?,?,?)");

        ps.setString(1, ad.getHeader());
        ps.setString(2, ad.getPubDate());
        ps.setString(3, ad.getText());

        ps.execute();

    }

    public boolean isExistWithThatText(String adText) throws SQLException {

        PreparedStatement ps = c.prepareStatement("select * from ad where ad_text = ?");

        ps.setString(1,adText);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        return rs.next();

    }



}
