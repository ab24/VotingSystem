package com.sap.voting.util;


import com.sap.voting.model.Person;
import com.sap.voting.model.VotingResponse;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.sap.voting.constants.TableNameConstants.SQL_CHECK_EXPIRATION;
import static com.sap.voting.constants.TableNameConstants.SQL_GET_POST_INFORMATION;
import static com.sap.voting.constants.TableNameConstants.SQL_GET_VOTE;

/**
 * Created by Arun on 9/18/17.
 */
public class SendEmail {

    /**
     * Cron job to run check for expired post and send email to the post owner
     * every hour. This cron can be made to run every 15minutes to every minute based on the
     * need for the system to respond to expored post.
     * @throws SQLException
     */
    @Scheduled(cron = "* 0 * * *  ")
    public void sendEmail() throws  SQLException{

        List<Integer>  postList = checkExpiration();
        for(int i :postList){
           VotingResponse votingResponse= getPostInformation(i);
            String receiverEmail=votingResponse.getEmail();
            String postName=votingResponse.getPostName();
            String postOption=votingResponse.getPostOption();
            sendEmail(receiverEmail,postName,postOption);
        }

    }

    /**
     * Method used to check the posts that are expired and an email flag is not
     * set to true (indicating that the email was sent)
     * @return
     * @throws SQLException
     */
    public List<Integer> checkExpiration() throws SQLException{
        List<Integer> postList = new ArrayList<>();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        //boolean count=false;
        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_CHECK_EXPIRATION);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                postList.add(rs.getInt(1));
            }

        } catch (SQLException se) {


        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return postList;
    }

    /**
     * Sends email to the post owner.
     * Here we use gmail's SMTP. If we use HANA we can integrate with SSO and we can send emails to the user.
     * The SSO integration was not implemented because the preferred feature contained registration of the user,
     * @param receiverEmail
     * @param postName
     * @param postOption
     */
    public void sendEmail(String receiverEmail, String postName, String postOption){
        final String userName = "testuser@gmail.com"; //sample email. Tested with original credentials.
        final String password = "password";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(receiverEmail));
            message.setSubject("Hurray!! Your voting period has ended. And we have your results!");
            message.setText("Dear post owner,"
                    + "\n\nThe winner of you post "+postName+"is   "+postOption+"!!!");

            Transport.send(message);



        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This method is used to get the expired post information such as
     * post owner's email, post name and option name which has maximum votes
     * @param postId
     * @return
     * @throws SQLException
     */
    public VotingResponse getPostInformation(int postId) throws SQLException{
        VotingResponse votingResponse = new VotingResponse();
        Connection connection = null;
        DataBaseConnection dbConnection = new DataBaseConnection();
        try {
            connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_POST_INFORMATION);
            preparedStatement.setInt(1,postId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                votingResponse.setEmail(rs.getString(1));
                votingResponse.setPostName(rs.getString(2));
                votingResponse.setPostOption(rs.getString(3));
            }

        } catch (SQLException se) {


        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return votingResponse;
    }
}
