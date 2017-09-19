package com.sap.voting.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by Arun on 9/16/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Post {
    private int postId;
    private String postName;
    private String postDescription;
    private List<PostOption> postOptionList;
    private String endDate;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public List<PostOption> getPostOptionList() {
        return postOptionList;
    }

    public void setPostOptionList(List<PostOption> postOptionList) {
        this.postOptionList = postOptionList;
    }

    public String getEndDate() {return endDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }
}
