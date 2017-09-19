package com.sap.voting.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by Arun on 9/16/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VotingResponse {

    private String sessionToken;
    private String Message;
    private boolean result;
    private List<PostOption> postOptions;
    private List<Post> post;
    private  List<Vote> vote;
    private List<Person> person;
    private String email;
    private String name;
    private String postName;
    private String postOption;
    //private  int postId;



    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<PostOption> getPostOptions() {
        return postOptions;
    }

    public void setPostOptions(List<PostOption> postOptions) {
        this.postOptions = postOptions;
    }

    public List<Post> getPost() {
        return post;
    }

    public void setPost(List<Post> post) {
        this.post = post;
    }

    public List<Vote> getVote() {
        return vote;
    }

    public void setVote(List<Vote> vote) {
        this.vote = vote;
    }

    public List<Person> getPerson() {
        return person;
    }

    public void setPerson(List<Person> person) {
        this.person = person;
    }

    /*public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostOption() {
        return postOption;
    }

    public void setPostOption(String postOption) {
        this.postOption = postOption;
    }
}
