package com.example.smartknock;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;

import java.util.Properties;

class PrimaryUserBehavior implements UserBehavior {
    private String loggedInUserEmail;
    private String loggedInUserPassword;

    public PrimaryUserBehavior(String email, String password) {
        this.loggedInUserEmail = email;
        this.loggedInUserPassword = password;
    }

    @Override
    public void executeFunctionality() {
        System.out.println("Executing functionality for Primary User...");
        // Specific behavior for primary users
    }

    // Method to send email invitations
    void sendEmailInvitation(String email) {
        // Set up the email properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP server for Gmail
        properties.put("mail.smtp.port", "587"); // Port for TLS/STARTTLS
        properties.put("mail.smtp.auth", "true"); // Enable authentication
        properties.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS

        // Use the logged-in user's credentials (email and password)
        final String username = loggedInUserEmail; // Primary user's email

        // Create a session with the email server
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Directly use loggedInUserPassword and convert to char[]
                return new PasswordAuthentication(username, loggedInUserPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Invitation to Join Doorbell");
            message.setText("Hello,\n\nYou have been invited to join the Doorbell app. Please follow the instructions to set up your account.");

            // Send the message
            Transport.send(message);

            System.out.println("Email successfully sent to: " + email);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email to: " + email);
        }
    }

// Method to send SMS invitations
    public void inviteSecondaryUserBySMS(String phoneNumber) {
        System.out.println("Sending SMS invitation to: " + phoneNumber);
        // Logic for sending an SMS invitation
    }

    // Method to manage user profiles (family, guest, admin)
    public void manageUserProfiles(String profileType) {
        System.out.println("Managing profile: " + profileType);
        // Logic to edit or remove user profiles
    }

    // Method to set time-based access restrictions
    public void setTimeRestrictions(String startTime, String endTime) {
        System.out.println("Setting time restrictions from " + startTime + " to " + endTime);
        // Logic for setting time restrictions
    }

    // Method to remove a user
    public void removeUser(String userId) {
        System.out.println("Removing user with ID: " + userId);
        // Logic to remove a user
    }
}
