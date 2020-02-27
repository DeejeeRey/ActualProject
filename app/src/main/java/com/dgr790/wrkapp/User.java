package com.dgr790.wrkapp;

public class User {

    private String userKey;
   private String userID;
   private String firstName;
   private String secondName;
   private String username;
   private String email;
   private int score;
   private int times;

   public User(String userID, String firstName, String secondName, String username, String email, int score, int times) {
        this.userID = userID;
        this.firstName = firstName;
        this.secondName = secondName;
        this.username = username;
        this.email = email;
        this.score = score;
        this.times = times;
   }

   public User() {

   }


   public String getUserKey() {
       return userKey;
   }

   public String getUserID() {
       return userID;
   }

   public String getFirstName() {
       return  firstName;
   }

   public String getSecondName() {
       return secondName;
   }

   public String getUsername() {
       return username;
   }

   public String getEmail() {
       return email;
   }

   public int getScore() {
       return score;
   }

   public int getTimes() {
       return times;
   }


   public void setUserKey(String userKey) {
       this.userKey = userKey;
   }

   public void setUserID(String userID) {
       this.userID = userID;
   }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
