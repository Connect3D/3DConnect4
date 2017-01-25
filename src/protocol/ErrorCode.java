package protocol;

public enum ErrorCode {

	UNKNOWN_ERROR, 
	COMMAND_UNSUPPORTED, 
	COMMAND_INVALID,
	NAME_UNAVAILABLE,
	FORBIDDEN,
	ILLEGAL_MOVE,
	SERVER_SHUTTING_DOWN;
	
	
	public String toString() {
		switch (this) {
			case UNKNOWN_ERROR:			return "ERROR 0";
			case COMMAND_UNSUPPORTED:	return "ERROR 1";
			case COMMAND_INVALID:		return "ERROR 2";
			case NAME_UNAVAILABLE:		return "ERROR 3";
			case FORBIDDEN:				return "ERROR 4";
			case ILLEGAL_MOVE:			return "ERROR 5";
			case SERVER_SHUTTING_DOWN:	return "ERROR 6";
		}
		return "";		// necessary because eclipse is scare of not returning a string
	}
	
}
