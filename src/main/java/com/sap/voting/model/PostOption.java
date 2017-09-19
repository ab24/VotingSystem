package com.sap.voting.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Arun on 9/16/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostOption {
    private String postOptionName;
    private String postOptionDescription;
    private int postOptionId;

    public String getPostOptionName() {
        return postOptionName;
    }

    public void setPostOptionName(String postOptionName) {
        this.postOptionName = postOptionName;
    }

    public String getPostOptionDescription() {
        return postOptionDescription;
    }

    public void setPostOptionDescription(String postOptionDescription) {
        this.postOptionDescription = postOptionDescription;
    }


    public int getPostOptionId() {
        return postOptionId;
    }

    public void setPostOptionId(int postOptionId) {
        this.postOptionId = postOptionId;
    }
}
