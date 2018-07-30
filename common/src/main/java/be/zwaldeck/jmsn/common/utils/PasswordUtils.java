package be.zwaldeck.jmsn.common.utils;

public final class PasswordUtils {

    public static int getPasswordStrength(String password) {

        int passwordPercentageStrength = 0;

        String[] passwordRegularExp = { // Your more password criteria goes here....

                ".*[a-z]+.*", // lower case
                ".*[A-Z]+.*", // upper case
                ".*[\\d]+.*", // digits
                ".*[@#$%]+.*" // symbols
        };

        if (password.matches(passwordRegularExp[0])) {
            passwordPercentageStrength += 25;
        }
        if (password.matches(passwordRegularExp[1])) {
            passwordPercentageStrength += 25;
        }
        if (password.matches(passwordRegularExp[2])) {
            passwordPercentageStrength += 25;
        }
        if (password.matches(passwordRegularExp[3])) {
            passwordPercentageStrength += 25;
        }

        return passwordPercentageStrength;
    }

}
