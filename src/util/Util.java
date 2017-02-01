package util;



public class Util {
	
	
	public static String join(String[] args) {
		String result = "";
		if (args != null && args.length > 0) {
			result += args[0];
			for (int i = 1; i < args.length; ++i) {
				result += " " + args[i];
			}
		}
		return result;
	}
	
	
	public static String[] split(String input) {
		if (input != null) {
			String[] splitted = input.split("\\s+");
			if (splitted != null && !(splitted.length == 1 && splitted[0] == "")) {
				return splitted;
			}
		}
		return new String[0];
	}
	
	
	public static boolean isInt(String input) {
	    try {
	        Integer.parseInt(input);
	    } catch (NumberFormatException e) {
	        return false;
	    }
	    return true;
	}
	
	
}