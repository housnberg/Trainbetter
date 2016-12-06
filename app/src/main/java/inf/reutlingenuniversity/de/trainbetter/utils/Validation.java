package inf.reutlingenuniversity.de.trainbetter.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eugen Ljavin
 */

public class Validation {

    public static final String EMAIL_REGEX = "^(.+)@(.+)$";

    public static boolean isUsernameValid(String username) {
        return !TextUtils.isEmpty(username) && username.length() > 2;
    }

    public static boolean isEmailValid(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && password.length() > 4;
    }
}
