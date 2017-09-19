package com.sap.voting.util;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.sap.voting.constants.DatabaseConstants.DRIVER;
import static com.sap.voting.constants.DatabaseConstants.URL;
import static com.sap.voting.constants.DatabaseConstants.PASSWORD;
import static com.sap.voting.constants.DatabaseConstants.USER;

/**
 * Created by Arun on 9/16/17.
 */

/**
 * Class contains custom JDBC connections methods.
 */
public  class DataBaseConnection {


    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Static method that returns the instance for the singleton
     *
     * @return {Connection} connection
     **/
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException at getConnection", e);
        } catch (SQLException e) {
            log.error("SQL Exception at getConnection", e);
        }
        return connection;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("SQL Exception at closeConnection", e);

            }
        }
    }

    public void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                log.error("SQL Exception at closePreparedStatement", e);
            }
        }
    }
}
