package org.example;

import jakarta.persistence.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Survey> surveys = new ArrayList<>();
    @Column(unique = true)
    private String username;
    private String hashedPassword;
    private String tempPassword;

    public Account(){
    }
    public Account(String username, String password){
        this.username = username;
        this.hashedPassword = hashPassword(password);
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();  // return the hashed password in hex format
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing the password", e);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean passwordMatch(String password){return hashedPassword.equals(hashPassword(password));}

    public String getHashedPassword(){return  hashedPassword;}

    public void setHashedPassword(String password){hashedPassword = hashPassword(password);}

    public List<Survey> getSurveys() {
        return surveys;
    }

    public void addSurvey(Survey survey) {
        surveys.add(survey);
    }

    public void setSurveys(List<Survey> surveys) {
        this.surveys = surveys;
    }
}
