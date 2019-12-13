package com.example.buildingconstraction.is229443.Model;

public class PostModel {
    String title;
    String imagePath;
    String Description;
    boolean approval;
    String Date;
    String postedBy;
    String postKey;
    String rejectMessage;

    public PostModel() {
    }

    public PostModel(String title, String imagePath, String description, boolean approval, String date, String postedBy) {
        this.title = title;
        this.imagePath = imagePath;
        Description = description;
        this.approval = approval;
        Date = date;
        this.postedBy = postedBy;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }
}
