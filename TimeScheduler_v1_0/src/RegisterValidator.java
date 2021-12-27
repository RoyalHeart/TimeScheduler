package src;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidator {
    private static final String USERNAME_PATTERN = "^[A-Za-z0-9_-]{3,15}$";
    private static final Pattern usernamePattern = Pattern.compile(USERNAME_PATTERN);

    // email regex pattern
    // (?=.{1,64}@) # local-part min 1 max 64

    // [A-Za-z0-9_-]+ # Start with chars in the bracket [ ], one or more (+)
    // # dot (.) not in the bracket[], it can't start with a dot (.)

    // (\\.[A-Za-z0-9_-]+)* # follow by a dot (.), then chars in the bracket [ ] one
    // or more (+)
    // # * means this is optional
    // # this rule for two dots (.)

    // @ # must contains a @ symbol

    // [^-] # domain can't start with a hyphen (-)

    // [A-Za-z0-9-]+ # Start with chars in the bracket [ ], one or more (+)

    // (\\.[A-Za-z0-9-]+)* # follow by a dot (.), optional

    // (\\.[A-Za-z]{2,}) # the last tld, chars in the bracket [ ], min 2
    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    private static final String NAME_PATTERN = "^[a-zA-Z]{0,30}$";
    private static final Pattern namePattern = Pattern.compile(NAME_PATTERN);

    private static final String PHONE_PATTERN = "^[0-9]{8,10}$";
    private static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);

    public static boolean isValidUsername(final String username) {
        Matcher matcher = usernamePattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isValidEmail(final String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidName(String name) {
        Matcher matcher = namePattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidPhone(String phone) {
        Matcher matcher = phonePattern.matcher(phone);
        return matcher.matches();
    }
}
