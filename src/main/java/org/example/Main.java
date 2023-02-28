package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*- create connection to dvdrental database
        - close connection
        - read username, password from environmental variables
        - print all tables from database
        - list actors


- query actors by first name start, open to sql injection attack
- use sql injection, to select actors where last_name is Harris
- use sql injection to access data from staff table
- fix sql injection using prepared statements*/

public class Main {


    private Connection con;

    public Main(Connection con) {

        this.con = con;
    }

    public static void main(String[] args) throws SQLException {
        String USERNAME = System.getenv("USER");
        String PASS = System.getenv("PASS");


        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dvdrental", USERNAME, PASS)) {
            System.out.println("Connection working");
            Main main = new Main(con);
            main.runQueries();


        } catch (SQLException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void runQueries() {
        try {
            printTables();
            printActors();
            System.out.println("----------------------------------------------------------------------");
            firstNameStart("asdsadsad%' OR last_name = 'Harris' --");
            System.out.println("----------------------------------------------------------------------");
            firstNameStart("asdsadsad%' UNION SELECT staff_id, first_name, last_name, null from staff --");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void printTables() throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
        List<String> tableColumn = new ArrayList<>();


        for (int i = 1; i <= tables.getMetaData().getColumnCount(); i++) {
            tableColumn.add(tables.getMetaData().getColumnName(i));
        }
        System.out.println(tableColumn);


        while (tables.next()) {
            System.out.println(tables.getString("TABLE_NAME"));
        }
    }

    public  void  printActors() throws SQLException{
        final String sql = "SELECT * FROM actor;";

        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()){
            System.out.println(resultSet.getInt("actor_id") + " "+ resultSet.getString("first_name") + " " +resultSet.getString("last_name"));
        }

    }

    public void firstNameStart(String firstName) throws SQLException{
       // final String sql = "SELECT * FROM actor WHERE first_name like '" + firstName+"%';";
        final String sql = "SELECT * FROM actor WHERE first_name like ?;";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setString(1,firstName);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            System.out.println(resultSet.getInt("actor_id") + " "+ resultSet.getString("first_name") + " " +resultSet.getString("last_name"));
        }
    }


}