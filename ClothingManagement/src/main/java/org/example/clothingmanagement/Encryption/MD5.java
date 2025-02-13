package org.example.clothingmanagement.Encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getName(String fullname) {
        String s = "";
        for (int i = fullname.length() - 1; i >= 0; i--) {
            if (fullname.charAt(i) == ' ') {
                break;
            }
            s = fullname.charAt(i) + s;
        }
        return s;
    }

    public static String getMd5(String input) {
        try {

            //tạo ra một đối tượng MessageDigest có khả năng tính toán giá trị băm MD5.
            MessageDigest md = MessageDigest.getInstance("MD5");

            //tính toán giá trị băm cho mảng byte của đầu vào
            byte[] messageDigest = md.digest(input.getBytes());

            //Mảng byte của giá trị băm MD5 được chuyển đổi thành một đối tượng BigInteger
            BigInteger no = new BigInteger(1, messageDigest);

            //chuyển đổi thành chuỗi cơ 16
            String hashtext = no.toString(16);
            // thêm các ký tự 0 vào phía trước cho đến khi đạt được độ dài 32 ký tự.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            // trả về 1 chuỗi được mã hóa md5
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
