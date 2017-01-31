package protocol;

import java.util.regex.Pattern;



public class Name {

	public static final int MIN_LENGTH = 1;
	public static final int MAX_LENGTH = 20;
	
	private static final Pattern REGEX_PATTERN = Pattern.compile("[A-Za-z0-9]{1,20}");
	
	
	public static boolean valid(String name) {
		if (name != null) {
			return REGEX_PATTERN.matcher(name).matches();
		}
		return false;
	}
	
}
