package com.sap.voting.dao;

import com.sap.voting.model.Post;
import com.sap.voting.model.PostOption;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arun on 9/17/17.
 */

/**
 * This class acts as a service. This service is responsible for all the
 * post related database accesses.
 */
@Service
public class PostDao {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public VotingResponse createPost(String sessionToken, String postName, String postDescription,
                                     String endDate, List<PostOption> postOptions) throws SQLException {
        log.info("Entering createPost method.");
        VotingResponse votingResponse = new VotingResponse();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        Post post=new Post();
        int id = UserUtil.getUserId(sessionToken);
        try {
            int postId=0;
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_POST);
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,postName);
            preparedStatement.setString(3,postDescription);
            preparedStatement.setString(4,endDate);
            preparedStatement.executeUpdate();
            preparedStatement= connection.prepareStatement(SQL_GET_POST_ID);
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,postName);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                postId=rs.getInt(1);
            }
            post.setPostId(postId);
            List<Post> postList= new ArrayList<>();
            postList.add(post);
            createPostOption(postId,postOptions);
            votingResponse.setMessage("Successfully created a post ");
            votingResponse.setSessionToken(sessionToken);
            votingResponse.setPost(postList);
            //votingResponse.setPostId(postId);
            votingResponse.setResult(true);
        } catch (SQLException se) {
            log.error("Houston! We have a problem while creating the post. e, "+se.getMessage());
            votingResponse.setMessage("Houston! We have a problem while creating the post. e, "+se.getMessage());
            votingResponse.setResult(false);

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        log.info("Exiting create post method");

        return  votingResponse;
    }

    public void createPostOption(int postId, List<PostOption> postOptions) throws SQLException{
        log.info("Entering createPostOption method");
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_POST_OPTIONS);
            preparedStatement.setInt(1,postId);
            for(PostOption postOption: postOptions){
                preparedStatement.setString(2,postOption.getPostOptionName());
                preparedStatement.setString(3,postOption.getPostOptionDescription());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

        } catch (SQLException se) {

            log.error("Houston! We have a problem while creating the post. e, "+se.getMessage());

        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        log.info("Exiting create post option method");

    }

    public VotingResponse modifyPostEndDate(String sessionToken, int postId, String endDate) throws SQLException{
        log.info("Entering modifyPostEndDate method");
        VotingResponse votingResponse = new VotingResponse();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        int id = UserUtil.getUserId(sessionToken);
        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_MODIFY_POST);
            preparedStatement.setInt(2,id);
            preparedStatement.setInt(3,postId);
            preparedStatement.setString(1,endDate);
            preparedStatement.executeUpdate();
            votingResponse.setMessage("Successfully modified the end date.");
            votingResponse.setResult(true);
        } catch (SQLException se) {
            log.error("Houston! We have a problem while modifying the post. e, "+se.getMessage());
            votingResponse.setMessage("Houston! We have a problem while modifying the post. e, "+se.getMessage());
            votingResponse.setResult(false);

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        log.info("Exiting modifyPostEndDate method");
        return  votingResponse;

    }

    public VotingResponse getAllPosts(String sessionToken) throws  SQLException{
        log.info("Entering getAllPosts method");
        List<Post> postList = new ArrayList<>();
        VotingResponse votingResponse = new VotingResponse();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        int id = UserUtil.getUserId(sessionToken);
        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_POST);
            preparedStatement.setInt(1,id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Post post = new Post();
                post.setPostId(rs.getInt(1));
                post.setPostName(rs.getString(2));
                post.setPostDescription(rs.getString(3));
                post.setEndDate(rs.getString(4));
                postList.add(post);

            }
            votingResponse.setPost(postList);
            votingResponse.setMessage("Successfully got all  posts.");
            votingResponse.setResult(true);
        } catch (SQLException se) {
            log.error("Houston! We have a problem in getAllPosts. e, "+se.getMessage());
            votingResponse.setMessage("Houston! We have a problem in getAllPosts. e, "+se.getMessage());
            votingResponse.setResult(false);

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        log.info("Exiting getAllPosts method");
        return  votingResponse;
    }


    public VotingResponse getPost(String sessionToken,int postId) throws  SQLException{
        log.info("Entering getPost method");
        List<Post> postList = new ArrayList<>();
        VotingResponse votingResponse = new VotingResponse();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        Post post = new Post();
        int id = UserUtil.getUserId(sessionToken);
        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_POST);
            preparedStatement.setInt(1,id);
            preparedStatement.setInt(2,postId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){

                post.setPostId(rs.getInt(1));
                post.setPostName(rs.getString(2));
                post.setPostDescription(rs.getString(3));


            }

            List<PostOption> postOptionList = getAllPostOptions(postId);
            post.setPostOptionList(postOptionList);
            postList.add(post);
            votingResponse.setPost(postList);
            votingResponse.setMessage("Successfully retrieved the post details the end date.");
            votingResponse.setResult(true);
        } catch (SQLException se) {
            log.error("Houston! We have a problem while retrieving the post details. e, "+se.getMessage());
            votingResponse.setMessage("Houston! We have a problem while retrieving the post details. e, "+se.getMessage());
            votingResponse.setResult(false);

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        log.info("Exiting getPost method");
        return  votingResponse;
    }

    public List<PostOption> getAllPostOptions(int postId) throws SQLException {
        log.info("Entering getAllPostOptions method");
        List<PostOption> postOptionList = new ArrayList<>();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_POSTOPTIONS);
            preparedStatement.setInt(1, postId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                PostOption postOption = new PostOption();
                postOption.setPostOptionId(rs.getInt(1));
                postOption.setPostOptionName(rs.getString(2));
                postOption.setPostOptionDescription(rs.getString(3));
                postOptionList.add(postOption);


            }

        } catch (SQLException se) {

            log.error("Error in getAllPostOptions method. e, "+se.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        log.info("Exiting getAllPostOptions method");
        return postOptionList;
    }

}
