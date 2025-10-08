package org.example.models;

public class User {
    private int id  = 0;
    private String user_name = "";
    private String email = null;
    private String hash_pass = null;
    private String pfp = null;

    public User() {}

    public User(int id, String user_name, String email, String hash_pass, String pfp) {
        this.id = id;
        this.user_name = user_name;
        this.email = email;
        this.hash_pass = hash_pass;
        this.pfp = pfp;
    }

    public boolean verify( String email,String hash_pass ) {
        return this.email.equals(email)&&this.hash_pass.equals(hash_pass);
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }
    public String getHash_pass() {
        return hash_pass;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }
}
