package com.noisevisionproduction.playmeetwebsite.service.validation;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ValidationService {

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(emailRegex);
    }

    public boolean arePasswordsTheSame(String firstPassword, String secondPassword) {
        return firstPassword.equals(secondPassword);
    }

    public boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char character : password.toCharArray()) {
            if (Character.isLetter(character)) {
                hasLetter = true;
            } else if (Character.isDigit(character)) {
                hasDigit = true;
            }
        }
        return hasLetter && hasDigit;
    }

    public void validateNicknameFormat(String nickName) {
        if (nickName.length() < 3 || nickName.length() > 30) {
            throw new IllegalArgumentException("Nazwa użytkownika musi mieć od 3 do 30 znaków.");
        }
    }

    public boolean isNicknameAvailable(String nickName, NicknameValidationService nicknameValidationService) {
        return nicknameValidationService.isNicknameAvailable(nickName).join();
    }

    public boolean isValidAge(String age) {
        try {
            int ageInt = Integer.parseInt(age);
            return ageInt >= 18 && ageInt <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidGender(String gender) {
        List<String> validGenders = Arrays.asList("Male", "Female", "Other");
        return validGenders.contains(gender);
    }
}
