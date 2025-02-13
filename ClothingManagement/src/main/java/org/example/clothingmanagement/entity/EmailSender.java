package org.example.clothingmanagement.entity;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class EmailSender {

    private static final String HOST = "smtp.gmail.com";  // Ví dụ sử dụng Gmail SMTP server
    private static final String FROM = "truongnvhe170937@fpt.edu.vn";  // Email người gửi
    private static final String PASSWORD = "Truong29062003@#";  // Mật khẩu email người gửi

    public static void sendEmail(String toEmail, String subject, String password, String employeeId) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", HOST); // SMTP Server của Gmail
        properties.put("mail.smtp.port", "587"); // Port cho TLS
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Tạo session gửi email
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }
        });

        // Soạn nội dung email
        String content = "Dear Employee,\n\n" +
                "Your account has been successfully created.\n" +
                "Employee ID: " + employeeId + "\n" +
                "Email: " + toEmail + "\n" +
                "Password: " + password + "\n\n" +
                "Please log in to your account.\n\n" +
                "Best regards,\nSherah Warehouse System";

        // Tạo đối tượng email
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setText(content);

        // Gửi email
        Transport.send(msg);
    }

    public static void sendPasswordChangedEmail(String toEmail, String newPassword) throws MessagingException {
        // Cấu hình gửi email
        Properties properties = new Properties();
        properties.put("mail.smtp.host", HOST); // SMTP Server của Gmail
        properties.put("mail.smtp.port", "587"); // Port cho TLS
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }
        });

        // Tạo nội dung email
        String subject = "Your Password has been Updated";
        String message = "Dear Staff,\n\n" +
                "Your password has been successfully updated. Below are your new login details:\n\n" +
                "Email: " + toEmail + "\n" +
                "New Password: " + newPassword + "\n\n" +
                "For your security, please change your password again after logging in.\n\n" +
                "Best regards,\nSherah Warehouse System";

        // Tạo đối tượng email
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setText(message);

        // Gửi email
        Transport.send(msg);
    }
}

