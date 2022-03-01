package JDBC;

import java.sql.*;

public class JDBC {
    final String jdbcURL = "jdbc:mysql://localhost:3306/javafx";
    final String jdbcUser = "root";
    final String jdbcPassword = "";
    public Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
    public Statement readData = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    public PreparedStatement writeData;
    public ResultSet resultSet;



    public JDBC(String selectedTable, String jdbcRegistrations) throws SQLException {
        resultSet = readData.executeQuery(selectedTable); //"select * from registrations"
        writeData = connection.prepareStatement(jdbcRegistrations); //sqlRequestInsertNewRegistration
    }



}