package com.sap.voting.dao;

import com.sap.voting.model.VotingResponse;
import com.sap.voting.util.DataBaseConnection;
import com.sap.voting.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.sap.voting.constants.TableNameConstants.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Arun on 9/17/17.
 */

/**
 * This class acts as a service. This service handles all the vote related database operations.
 */
@Service
public class VoteDao {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Cast vote method is used to create entry to the vote database for the
     * given userId(obtained from sessionToken), post id and post option id selected
     * by the user.
     * @param sessionToken
     * @param postId
     * @param postOptionId
     * @return
     * @throws SQLException
     */
    public VotingResponse castVote(String sessionToken, int postId, int postOptionId) throws SQLException {
        log.info("Entering castVote method! ");
        VotingResponse votingResponse = new VotingResponse();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        int userId= UserUtil.getUserId(sessionToken);
        Boolean count = isVoted(postId,userId);
        if(!count) {
            try {
                connection = dbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_CAST_VOTE);
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, postId);
                preparedStatement.setInt(3, postOptionId);
                preparedStatement.executeUpdate();
                votingResponse.setMessage("Successfully VOTED your favorite option. You can come back and modify your option before the voting ends!");
                votingResponse.setResult(true);
            } catch (SQLException se) {
                log.error("Houston! We have a problem while casting the vote. e, " + se.getMessage());
                votingResponse.setMessage("Houston! We have a problem while casting the vote. e, " + se.getMessage());
                votingResponse.setResult(false);

            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }else{
            log.info("You have already voted for this post! ");
            votingResponse.setResult(false);
            votingResponse.setMessage(("You have already voted for this post! "));
        }
        log.info("Exiting castVote Method");

        return  votingResponse;
    }

    /**
     * isVoted method checks whether the user has already voted.
     * This check avoids duplicate entries to the table.
     * @param userId
     * @param postId
     * @return
     * @throws SQLException
     */
    public Boolean isVoted(int userId,int postId) throws SQLException{
        log.info("Entering isVoted method");
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        boolean count=false;
        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_VOTE);
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,postId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                count = rs.getBoolean(1);
            }

        } catch (SQLException se) {

            log.error("Houston! We have a problem while checking isVoted. e, " + se.getMessage());

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        log.info("Exiting isVoted method");
        return count;
    }

    /**
     * Modify vote method is used to modify the existing vote if the time has not crossed the end date.
     * If the user has not voted then a vote will be casted
     * @param sessionToken
     * @param postId
     * @param postOptionId
     * @return
     * @throws SQLException
     */
    public VotingResponse modifyVote(String sessionToken, int postId, int postOptionId) throws SQLException{
        log.info("Entering modifyVote method");
        VotingResponse votingResponse = new VotingResponse();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        int userId=UserUtil.getUserId(sessionToken);
        Boolean isActive = isExpired(postId);
        if(isActive){
            log.info("Vote can not be casted or modified since the voting period is closed!");
            votingResponse.setResult(false);
            votingResponse.setMessage("Vote can not be casted or modified since the voting period is closed! ");
            return votingResponse;
        }
        Boolean count = isVoted(postId,userId);
        if(count) {
            try {
                connection = dbConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_MODIFY_VOTE);
                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, postId);
                preparedStatement.setInt(1, postOptionId);

                preparedStatement.executeUpdate();
                votingResponse.setMessage("Successfully you modified your post option!");
                votingResponse.setResult(true);
            } catch (SQLException se) {
                log.info("Houston! We have a problem while modifying the vote details. e, " + se.getMessage());
                votingResponse.setMessage("Houston! We have a problem while modifying the vote details. e, " + se.getMessage());
                votingResponse.setResult(false);

            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }else{
            votingResponse = castVote(sessionToken,postId,postOptionId);
        }
        log.info("Exiting modify vote");

        return  votingResponse;
    }

    /**
     * Method to check whether the post has expired.
     * @param postId
     * @return
     * @throws SQLException
     */
    public boolean isExpired(int postId) throws SQLException{
        log.info("Entering isExpired");
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();

        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_POST_IS_EXPIRED);
            preparedStatement.setInt(1,postId);
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()) {
                int count = rs.getInt(1);
                if (count <= 0) {
                    log.info("Exiting is expired");
                    return true;
                }
            }

        } catch (SQLException se) {

            log.error("Houston! We have a problem while retrieving the post details. e, "+se.getMessage());

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        log.info("Exiting is expired");
        return false;
    }

}
