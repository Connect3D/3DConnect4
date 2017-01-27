package protocol;

import com.google.common.collect.EnumHashBiMap;



public enum Error implements Command {

	UNKNOWN_ERROR, 
	COMMAND_UNSUPPORTED, 
	COMMAND_INVALID,
	NAME_UNAVAILABLE,
	FORBIDDEN,
	ILLEGAL_MOVE,
	SERVER_SHUTTING_DOWN;
	
	
	public String toString() {
		return ERRORS.get(this);
	}
	
	
	// use the parse function of Command outside of package
	protected static Error parse(String s) {
		return ERRORS.inverse().get(s);
	}
	
	
	protected static final EnumHashBiMap<Error, String> ERRORS = EnumHashBiMap.create(Error.class);
	
	static {
		ERRORS.put(UNKNOWN_ERROR,        "ERROR 0");
		ERRORS.put(COMMAND_UNSUPPORTED,  "ERROR 1");
		ERRORS.put(COMMAND_INVALID,      "ERROR 2");
		ERRORS.put(NAME_UNAVAILABLE,     "ERROR 3");
		ERRORS.put(FORBIDDEN,            "ERROR 4");
		ERRORS.put(ILLEGAL_MOVE,         "ERROR 5");
		ERRORS.put(SERVER_SHUTTING_DOWN, "ERROR 6");
	}
	
}
