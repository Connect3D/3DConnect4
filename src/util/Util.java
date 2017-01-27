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
	
	
	public static <T> boolean arrayStartEquals(T[] t1, T[] t2) {
		for (int i = 0; i < (t1.length < t2.length ? t1.length : t2.length); ++i) {
			if (!t1[i].equals(t2[i])) {
				return false;
			}
		}
		return true;
	}
	
	
	public static boolean isInt(String input){
	    try{
	        Integer.parseInt(input);
	    }catch(NumberFormatException e){
	        return false;
	    }
	    return true;
	}
	
	
	public static String[] arrayTrim(String[] in) {
		ArrayList<String> out = new ArrayList<String>();
		for (int i = 0; i < in.length; ++i) {
			String s = in[i].trim();
			if (!s.equals("")) {
				out.add(s);
			}
		}
		return (String[]) out.toArray();
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
