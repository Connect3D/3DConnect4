package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Util {
	
	
	public static <T> boolean contains(T[] array, T test) {
		for (T t : array) {
			if (t.equals(test)) {
				return true;
			}
		}
		return false;
	}
	
	
	public static String join(String[] args) {
		String result = "";
		for (String arg : args) {
			result += " " + arg;
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
	
	
	public static boolean isInt(String input){
	    try{
	        Integer.parseInt(input);
	    }catch(NumberFormatException e){
	        return false;
	    }
	    return true;
	}
	
	
	public static int createPort(String port) {
		int portInt = 0;
		try {
			portInt = Integer.parseInt(port);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: port " + port + " is not an integer.");
			System.exit(0);
		}
		return portInt;
	}

	
	public static InetAddress createAddress(String host) {
		InetAddress address = null;
		try {
			address = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			System.out.println("ERROR: Localhost " + host + " not found");
			System.exit(0);
		}
		return address;
	}
	
	
}
