package com.sap.voting.dao;

import com.sap.voting.model.VotingResponse;
import com.sap.voting.util.DataBaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.UUID;
import static com.sap.voting.constants.TableNameConstants.*;

/**
 * Created by Arun on 9/16/17.
 */

/**
 * This class acts as a service. This class handles all the user related
 * databas accesses.
 */
@Service
public class UserDao {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * This method is used to register user to the system.
     * @param name
     * @param userName
     * @param password
     * @return
     * @throws SQLException
     */
    public VotingResponse registerUser(String name, String userName, String password) throws SQLException{
        log.info("Entering registerUser Method. ");
        VotingResponse votingResponse = new VotingResponse();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        boolean ifUserExits = isUserExist(userName,password);
        if(!ifUserExits) {
            try {
                connection = dbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_USER);
                preparedStatement.setString(1,name);
                preparedStatement.setString(2,userName);
                preparedStatement.setString(3,password);
                preparedStatement.executeUpdate();
                votingResponse.setMessage("Successfully registered to the voting ");
                votingResponse.setResult(true);
            } catch (SQLException se) {
                log.error("Unsuccessful registration to the voting. e, "+se.getMessage());
                votingResponse.setMessage("Unsuccessful registration to the voting. e, "+se.getMessage());
                votingResponse.setResult(false);

            } finally {
                if (connection != null) {
                    connection.close();
                }
            }

        }else{
            log.info("Unsuccessful registration user already exists");
            votingResponse.setMessage("Unsuccessful registration user already exists");
            votingResponse.setResult(false);
        }
        log.info("Successful completion of register method.");
        return  votingResponse;
    }

    /**
     * This method is used to check whether the user already exists in the system.
     * @param userName
     * @param password
     * @return
     * @throws SQLException
     */
    public boolean isUserExist(String userName, String password) throws SQLException{
        log.info("Start of isUserExist method");
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        try{
            int count =0;
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CHECK_USER);
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,password);
            ResultSet rs =preparedStatement.executeQuery();
            while(rs.next()){
                count= rs.getInt(1);
            }

            if(count > 0){
                return true;
            }
        }catch (SQLException se){
            log.error("Houston we have a problem @ isUserExist method. e, "+se.getMessage());

        }finally {
            if( connection != null){
                connection.close();
            }
        }
        log.info("Successful completion of isUserExist method");
        return false;

    }

    /**
     * Method used to allow user to log on to the system and returns a
     * sessionToken which can be used throughout the session.
     * SessionToken is used because REST is stateless.
     * @param userName
     * @param password
     * @return
     * @throws SQLException
     */

    public VotingResponse login(String userName, String password) throws SQLException{
        log.info("Start of login method ");
        VotingResponse votingResponse = new VotingResponse();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        String sessionToken= UUID.randomUUID().toString();
            boolean userExists=isUserExist(userName,password);
        if(userExists) {
            try {
                connection = dbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_LOG_IN_USER);
                preparedStatement.setString(2, userName);
                preparedStatement.setString(3, password);
                preparedStatement.setString(1, sessionToken);
                preparedStatement.executeUpdate();
                votingResponse.setMessage("Successfully logged on  to the voting ");
                votingResponse.setSessionToken(sessionToken);
                votingResponse.setResult(true);
            } catch (SQLException se) {
                log.error("Unsuccessful log on to the voting. e, " + se.getMessage());
                votingResponse.setMessage("Unsuccessful log on to the voting. e, " + se.getMessage());
                votingResponse.setResult(false);

            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }else{
            votingResponse.setResult(false);
            votingResponse.setMessage("The user is either not registered to the system or the userName and password is not valid! ");
        }
        log.info("Completion of login method");
        return  votingResponse;

    }

    /**
     * Method is used to logout of the system.
     * It clears the sessionToken marking end of a user session.
     * @param sessionToken
     * @return
     * @throws SQLException
     */
    public VotingResponse logout(String sessionToken) throws SQLException{
        log.info("Start of logout method");
        VotingResponse votingResponse = new VotingResponse();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_LOG_OUT_USER);
            preparedStatement.setString(1,"");
            preparedStatement.setString(2,sessionToken);
            preparedStatement.executeQuery();
            votingResponse.setMessage("Successfully log out  to the voting system");
            votingResponse.setSessionToken(sessionToken);
            votingResponse.setResult(true);
        } catch (SQLException se) {
            log.error("Unsuccessful log out to the voting. e, "+se.getMessage());
            votingResponse.setMessage("Unsuccessful log out to the voting system. e, "+se.getMessage());
            votingResponse.setResult(false);

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        log.info("Completion of logout method");
        return  votingResponse;
    }



}
