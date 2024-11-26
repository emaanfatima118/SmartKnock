package com.example.smartknock;

import java.util.Date;

public class VisitorView {
        private String name;
        private boolean isFrequent;
        private Date datetime;
        private String imageUrl;
        private String id;

        public VisitorView(String id,boolean isFrequent, Date datetime,String imageUrl) {
            this.isFrequent = isFrequent;
            this.datetime = datetime;
            this.imageUrl = imageUrl;
            this.id = id;
            this.name="Visitor";
        }
        public Date getDatetime() {
            return datetime;
        }

        public void setDatetime(Date datetime) {
            this.datetime = datetime;
        }

        public boolean isFrequent() {
            return isFrequent;
        }

        public String getId() {
            return id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
        public void setFrequent(boolean frequent) {
            isFrequent = frequent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            if(isFrequent())
                this.name = name;
        }
    }

