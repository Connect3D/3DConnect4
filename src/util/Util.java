package util;

public class Util {

	
	public static boolean contains(String[] array, String test) {
		for (String s : array) {
			if (s.equals(test)) {
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean isInt(String input){
	    try{
	        Integer.parseInt(input);
	    }catch(NumberFormatException e){
	        return false;
	    }
	    return true;
	}
	
	
}
