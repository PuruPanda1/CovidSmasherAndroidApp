package com.purupanda.login.models;

public class commentModel {
    private String commentUsername;
    private String commentDescription;
    private String commentId;
    private String dateTime;
    private String commentUserId;

    public commentModel(String commentUsername, String commentDescription, String commentId, String dateTime, String commentUserId) {
        this.commentUsername = commentUsername;
        this.commentDescription = commentDescription;
        this.commentId = commentId;
        this.dateTime = dateTime;
        this.commentUserId = commentUserId;
    }

    public String getCommentUsername() {
        return commentUsername;
    }

    public void setCommentUsername(String commentUsername) {
        this.commentUsername = commentUsername;
    }

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }
}
