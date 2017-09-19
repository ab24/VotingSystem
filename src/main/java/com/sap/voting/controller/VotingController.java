package com.sap.voting.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sap.voting.dao.PostDao;
import com.sap.voting.dao.UserDao;
import com.sap.voting.dao.VoteDao;
import com.sap.voting.model.PostOption;
import com.sap.voting.model.VotingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arun on 9/16/17.
 */

/**
 * Controller. The single point of entry to the system.
 * Controller controls the flow of the data and decisions.
 */

@Controller
@Service
@PropertySource("classpath:/application.properties")
@RequestMapping("/voting")
public class VotingController {
    @Autowired
    UserDao userDao;

    @Autowired
    PostDao postDao;

    @Autowired
    VoteDao voteDao;

    @RequestMapping(value ="/test/", method=RequestMethod.GET)
    public @ResponseBody String Test(
            @RequestParam( value="userName", required=true) String userName,
            @RequestParam(value="password", required=true) String password,
            @RequestParam(value="name", required=true) String name
    ) throws SQLException{
        return "HELLO WORLD! ";


    }

    @RequestMapping(value ="/authorization/register", method=RequestMethod.POST)
    public @ResponseBody VotingResponse register(@RequestBody String requestBody
    ) throws SQLException{
        VotingResponse votingResponse= new VotingResponse();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(requestBody);
        String userName=  jsonObject.get("email").toString().replaceAll("\"","");
        String password=  jsonObject.get("password").toString().replaceAll("\"","");
        String name= jsonObject.get("name").toString().replaceAll("\"","");
        if(userName != null && !userName.isEmpty() && password !=null && !password.isEmpty()
               && name != null && !name.isEmpty()) {
            votingResponse = userDao.registerUser(name, userName, password);
        }else{
            votingResponse.setResult(false);
            votingResponse.setMessage("Email or Password or Name  cannot be empty! ");
        }
        return votingResponse;


    }

    @RequestMapping(value ="/authorization/login", method= RequestMethod.POST)
    public @ResponseBody VotingResponse login( @RequestBody String requestBody
    ) throws SQLException{
        VotingResponse votingResponse= new VotingResponse();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(requestBody);
        String userName=  jsonObject.get("email").toString().replaceAll("\"","");
        String password=  jsonObject.get("password").toString().replaceAll("\"","");
        if(userName != null && !userName.isEmpty() && password !=null && !password.isEmpty()) {
             votingResponse = userDao.login(userName, password);
        }else{
            votingResponse.setResult(false);
            votingResponse.setMessage("Email or Password cannot be empty! ");
        }
        return votingResponse;


    }

    @RequestMapping(value ="/authorization/logout", method=RequestMethod.PUT)
    public @ResponseBody VotingResponse logout(@RequestBody  String requestBody) throws SQLException{
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject)jsonParser.parse(requestBody);
        String sessionToken = jsonObject.get("sessionToken").toString().replaceAll("\"","");
        VotingResponse votingResponse=userDao.logout(sessionToken);
        return votingResponse;


    }

    @RequestMapping(value ="/post/create", method= RequestMethod.POST)
    public @ResponseBody VotingResponse createPost(@RequestBody String requestBody) throws SQLException{
        JsonParser jsonParser = new JsonParser();
        List<PostOption> postOptionList = new ArrayList<>();
        JsonObject jsonObject =(JsonObject) jsonParser.parse(requestBody);
        String sessionToken=jsonObject.get("sessionToken").toString().replaceAll("\"","");
        String postName=jsonObject.get("postName").toString().replaceAll("\"","");
        String postDescription=jsonObject.get("postDescription").toString().replaceAll("\"","");
        String endDate=jsonObject.get("endDate").toString().replaceAll("\"","");
        JsonArray optionsArray;
        optionsArray=jsonObject.getAsJsonArray("postOptions");
        for(int i =0; i < optionsArray.size();i++){
            JsonObject object= (JsonObject)optionsArray.get(i);
            PostOption postOption= new PostOption();
            postOption.setPostOptionName(object.get("postOptionName").toString().replaceAll("\"",""));
            postOption.setPostOptionDescription(object.get("postOptionDescription").toString().replaceAll("\"",""));
            postOptionList.add(postOption);

        }
        VotingResponse votingResponse=postDao.createPost(sessionToken,postName,postDescription,endDate,postOptionList);
        return votingResponse;


    }

    @RequestMapping(value ="/post/modify", method= RequestMethod.PUT)
    public @ResponseBody VotingResponse modifyEndDate(@RequestBody String requestBody) throws SQLException{
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject =(JsonObject) jsonParser.parse(requestBody);
        String sessionToken=jsonObject.get("sessionToken").toString().replaceAll("\"","");
        int postId=Integer.valueOf(jsonObject.get("postId").toString().replaceAll("\"",""));
        String endDate=jsonObject.get("endDate").toString().replaceAll("\"","");
        VotingResponse votingResponse=postDao.modifyPostEndDate(sessionToken,postId,endDate);
        return votingResponse;


    }

    @RequestMapping(value ="/post/get/All", method= RequestMethod.GET)
    public @ResponseBody VotingResponse getAllPost(@RequestParam(value="sessionToken", required=true) String sessionToken) throws SQLException{
        VotingResponse votingResponse=postDao.getAllPosts(sessionToken);
        return votingResponse;


    }

    @RequestMapping(value ="/post/option/get/All", method= RequestMethod.GET)
    public @ResponseBody VotingResponse getAllPostOptions( @RequestParam(value="postId", required=true) int postId) throws SQLException{
        VotingResponse votingResponse= new VotingResponse();
        List<PostOption> postOptionList=postDao.getAllPostOptions(postId);
        votingResponse.setPostOptions(postOptionList);
        votingResponse.setResult(true);
        return votingResponse;


    }

    @RequestMapping(value ="/post/get/", method= RequestMethod.GET)
    public @ResponseBody VotingResponse getPost(@RequestParam(value="sessionToken", required=true) String sessionToken,
                                                @RequestParam(value="postId", required=true) int  postId) throws SQLException{
         VotingResponse votingResponse=postDao.getPost(sessionToken,postId);
        return votingResponse;


    }


    @RequestMapping(value ="/post/vote/cast", method= RequestMethod.POST)
    public @ResponseBody VotingResponse castVote(@RequestBody String requestBody) throws SQLException{
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject =(JsonObject) jsonParser.parse(requestBody);
        String sessionToken=jsonObject.get("sessionToken").toString().replaceAll("\"","");
        int postId=Integer.valueOf(jsonObject.get("postId").toString().replaceAll("\"",""));
        int postOptionId=Integer.valueOf(jsonObject.get("postOptionId").toString().replaceAll("\"",""));
        VotingResponse votingResponse=voteDao.castVote(sessionToken,postId,postOptionId);
        return votingResponse;


    }

    @RequestMapping(value ="/post/vote/modify", method= RequestMethod.POST)
    public @ResponseBody VotingResponse modifyVote( @RequestBody String requestBody) throws SQLException{
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject =(JsonObject) jsonParser.parse(requestBody);
        String sessionToken=jsonObject.get("sessionToken").toString().replaceAll("\"","");
        int postId=Integer.valueOf(jsonObject.get("postId").toString().replaceAll("\"",""));
        int postOptionId=Integer.valueOf(jsonObject.get("postOptionId").toString().replaceAll("\"",""));
        VotingResponse votingResponse=voteDao.modifyVote(sessionToken,postId,postOptionId);
        return votingResponse;


    }



}
