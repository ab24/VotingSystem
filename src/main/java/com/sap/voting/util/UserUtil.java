package com.sap.voting.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.RescaleOp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static com.sap.voting.constants.TableNameConstants.*;
/**
 * Created by Arun on 9/16/17.
 */

/**
 * Util class to handle user related util functions
 */
public class UserUtil {

    protected final static Logger log = LoggerFactory.getLogger(UserUtil.class);

    /**
     * Method to get userId based on the sessionToken
     * @param sessionToken
     * @return
     * @throws SQLException
     */
    public  static int getUserId(String sessionToken) throws SQLException{
        log.info("Entering getUserId");
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        int userId=-1;
        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_USER_ID);
            preparedStatement.setString(1,sessionToken);
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()){
                userId=rs.getInt(1);
            }

        } catch (SQLException se) {
            log.error("Exception in getUserId. e,  "+se.getMessage());

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        log.info("Exiting getUserId");
        return  userId;
    }
}
