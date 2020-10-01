package database;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseHelpers {
	public static boolean isValidID(String id) {
		// Regex to check valid identifier. https://www.geeksforgeeks.org/how-to-validate-identifier-using-regular-expression-in-java/
        String regex = "^([a-zA-Z_$][a-zA-Z\\d_$]*)$";
        Pattern p = Pattern.compile(regex);

        if (id == null) {
            return false;
        }

        Matcher m = p.matcher(id);

        return m.matches();
	}
}
