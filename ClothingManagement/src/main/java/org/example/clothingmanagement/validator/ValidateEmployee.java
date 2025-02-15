package org.example.clothingmanagement.validator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateEmployee {

    // Validate full name
    public static boolean isValidFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return false; // Không được để trống
        }
        String regex = "^(?!\\s)(?!.*\\s{2,})[a-zA-ZÀ-ỹ\\s]+$";
        return Pattern.matches(regex, fullName);
    }

    // Validate phone number
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false; // Không được để trống
        }
        String regex = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-4|6-9])[0-9]{7}$";
        return Pattern.matches(regex, phone);
    }

    // Validate address
    public static boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false; // Không được để trống
        }

        // Địa chỉ không được chứa ký tự đặc biệt (ngoài , . - /)
        String regex = "^[a-zA-Z0-9À-ỹ\\s,.-/]+$";
        return Pattern.matches(regex, address);
    }

    public static boolean isAdult(String dob) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = sdf.parse(dob);

            Calendar today = Calendar.getInstance();
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTime(dateOfBirth);

            int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
            if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
                    (today.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
                age--; // Chưa đến sinh nhật trong năm nay
            }
            return age >= 18;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
