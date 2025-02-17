package org.example.clothingmanagement.entity;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {
    private final String eFrom = "datpdhs176052@fpt.edu.vn";
    private final String ePass = "mnlccbsilspnbfyf";

    //
    // check email
    public boolean isValidEmail(String email) {
        // Biểu thức chính quy cho định dạng email
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        // Tạo đối tượng Pattern.
        Pattern pattern = Pattern.compile(emailRegex);
        // Tạo đối tượng Matcher
        Matcher matcher = pattern.matcher(email);
        // Kiểm tra chuỗi với biểu thức chính quy
        return matcher.matches();
    }

    public void sendEmail(String subject, String messgage, String to) {
        try {
        } catch (Exception e) {
        }
        // Properties
        Properties props = new Properties();
        //Su dung server nao de gui mail- smtp host
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Phải có cái này

        //dang nhap tai khoan
        Authenticator au = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(eFrom, ePass); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
            }

        };
        // phien lam viec
        Session session = Session.getInstance(props, au);
        try {
            MimeMessage msg = new MimeMessage(session);
            //kieu noi dung
            msg.addHeader("Content-type", "text/HTML, charset=UTF-8");
            //nguoi gui
            msg.setFrom(new InternetAddress(eFrom));
            //nguoi nhan
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            // tieu de
            msg.setSubject(subject, "UTF-8");
            // Noi dung
            msg.setContent(messgage, "text/html; charset=UTF-8");
            // Gui email
            Transport.send(msg);
            System.out.println("Sending email to: " + to);

        } catch (Exception e) {
            System.out.println("Send email failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Select subject to send email

    public String subjectForgotPass() {
        return "Support forgot password";
    }

    // Select content to send email
    public String messageForgotPass(int code) {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>Password Reset Code</title>\n"
                + "</head>\n"
                + "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;\">\n"
                + "\n"
                + "    <table style=\"width: 100%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-collapse: collapse;\">\n"
                + "        <tr>\n"
                + "            <td style=\"padding: 20px; text-align: center; background-color: #4CAF50; color: #ffffff; font-size: 24px;\">\n"
                + "                Password Reset Code\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "        <tr>\n"
                + "            <td style=\"padding: 20px;\">\n"
                + "                <p>Hello </p>\n"
                + "                <p>You have requested to reset your password. Please use the following code to reset your password:</p>\n"
                + "                <p style=\"font-size: 28px; font-weight: bold; color: #4CAF50;\">" + code + "</p>\n"
                + "                <p>If you didn't request this, you can safely ignore this email.</p>\n"
                + "                <p>Thank you!</p>\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "        <tr>\n"
                + "            <td style=\"padding: 20px; text-align: center; background-color: #4CAF50; color: #ffffff;\">\n"
                + "                &copy; 2024 sportfit\n"
                + "            </td>\n"
                + "        </tr>\n"
                + "    </table>\n"
                + "\n"
                + "</body>\n"
                + "</html>";
    }

    public  void sendEmail(String toEmail, String subject, String password, String employeeId) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Server của Gmail
        properties.put("mail.smtp.port", "587"); // Port cho TLS
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Tạo session gửi email
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(eFrom, ePass);
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
        msg.setFrom(new InternetAddress(eFrom));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setText(content);

        // Gửi email
        Transport.send(msg);
    }

    public void sendPasswordChangedEmail(String toEmail, String newPassword) throws MessagingException {
        // Cấu hình gửi email
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Server của Gmail
        properties.put("mail.smtp.port", "587"); // Port cho TLS
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(eFrom, ePass);
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
        msg.setFrom(new InternetAddress(eFrom));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setText(message);

        // Gửi email
        Transport.send(msg);
    }
}
