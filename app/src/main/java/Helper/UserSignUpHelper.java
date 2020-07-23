package Helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSignUpHelper {

    boolean result = false;

    public boolean isPasswordValid(String password)
    {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(password);

        result = m.matches();

        return  result;
    }
}
