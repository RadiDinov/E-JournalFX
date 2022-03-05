package JDBC;

import java.sql.*;

public class JDBC { //Adding JDBC to the project as object

    final String jdbcURL = "jdbc:mysql://localhost:3306/javafx";
    final String jdbcUser = "root";
    final String jdbcPassword = "";
    public Connection connection = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
    public Statement readData = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    public Statement statement;
    public PreparedStatement writeData;
    public ResultSet resultSet;



    public JDBC(String selectedTable, String jdbcRegistrations) throws SQLException {
        resultSet = readData.executeQuery(selectedTable); //"select * from registrations"
        writeData = connection.prepareStatement(jdbcRegistrations); //sqlRequestInsertNewRegistration
    }

    public JDBC(String resultSet) throws SQLException {
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        this.resultSet = statement.executeQuery(resultSet); //Show tables
    }

    public ResultSet getResultSet() {
        return resultSet;
    }
}