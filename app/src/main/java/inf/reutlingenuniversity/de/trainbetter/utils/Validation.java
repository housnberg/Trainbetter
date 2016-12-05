package inf.reutlingenuniversity.de.trainbetter.utils;

/**
 * @author Eugen Ljavin
 */

public class Validation {

    public static boolean isUsernameValid(String username) {
        return username.length() > 2;
    }

    public static boolean isEmailValid(String email) {
        return email.length() > 2;
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
