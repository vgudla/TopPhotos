package com.groupon.vgudla.topphotos.dto;

import java.io.Serializable;

public class Comment implements Serializable {
    private String userName;
    private String commentText;

    public Comment(String userName, String commentText) {
        this.userName = userName;
        this.commentText = commentText;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentText() {
        return commentText;
    }
}
