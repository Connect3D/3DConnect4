package protocol;

import server.Server;

public class Name {

	public static final String VALID_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static final int MIN_LENGTH = 1;
	public static final int MAX_LENGTH = 20;
	
	
	public static boolean valid(String name) {
		if (name.length() >= MIN_LENGTH && name.length() <= MAX_LENGTH && !name.equals(Server.NAME)) {
			for (int i = 0; i < name.length(); ++i) {
				if (!VALID_CHARACTERS.contains(name.substring(i, i+1))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
