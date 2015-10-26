package com.groupon.vgudla.topphotos.dto;

import java.util.List;

public class Photo {
    private String userName;
    private String userProfileUrl;
    private String imageUrl;
    private String captionText;
    private int imageHeight;
    private int imageWidth;
    private int likes;
    private String createdTime;
    private List<Comment> commentsList;
    private int commentCount;
    private String id;

    public Photo(String userName, String imageUrl, String captionText, int imageHeight,
                 int likes, int imageWidth, String userProfileUrl, String createdTime,
                 List<Comment> commentsList, int commentCount, String id) {
        this.userName = userName;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.captionText = captionText;
        this.userProfileUrl = userProfileUrl;
        this.createdTime = createdTime;
        this.commentsList = commentsList;
        this.commentCount = commentCount;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCaptionText() {
        return captionText;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getLikes() {
        return likes;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public List<Comment> getCommentsList() {
        return commentsList;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getId() {
        return id;
    }
}
