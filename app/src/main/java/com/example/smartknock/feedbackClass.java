package com.example.smartknock;

public class feedbackClass {
        private String userId;
        private String fid;
        private String comments;
        private int rating;

        // Default constructor (required for Firebase)
        public feedbackClass() {}

        // Constructor
        public feedbackClass(String feedbackid,String userId, String comments, int rating) {
            this.userId = userId;
            this.comments = comments;
            this.rating = rating;
            this.fid=feedbackid;
        }

        // Getters and setters
        public String getUserId() {
            return userId;
        }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

}
