package protocol.command;

import java.util.EnumMap;
import java.util.regex.Pattern;

import com.google.common.collect.EnumHashBiMap;

import util.exception.*;



public enum Error implements Command {

	UNKNOWN_ERROR, 
	COMMAND_UNSUPPORTED, 
	COMMAND_INVALID,
	NAME_UNAVAILABLE,
	FORBIDDEN,
	ILLEGAL_MOVE,
	SERVER_SHUTTING_DOWN;
	
	
	public String toString() {
		return NAME.get(this);
	}
	
	
	public static Error parse(String[] command) throws CommandUnsupportedException, CommandInvalidException {
		if (command != null && command.length > 1) {
			if (command[0].equals("ERROR")) {
				Error result = NAME.inverse().get(command[0] + " " + command[1]);
				if (result != null) {
					return result;
				}
				throw new CommandInvalidException();
			}
		}
		throw new CommandUnsupportedException();
	}
	
	
	public static boolean isValid(Error e, String s, Command.Direction d) throws CommandForbiddenException, CommandInvalidException {
		if (d != Command.Direction.BIDIRECTIONAL && (DIRECTION.get(e) == d || DIRECTION.get(e) == Command.Direction.BIDIRECTIONAL)) {
			if (PATTERN.get(e).matcher(s).matches()) {
				return true;
			}
			throw new CommandInvalidException();
		}
		throw new CommandForbiddenException();
	}
	
	
	public static final EnumHashBiMap<Error, String> NAME = EnumHashBiMap.create(Error.class);
	public static final EnumMap<Error, Command.Direction> DIRECTION = new EnumMap<Error, Command.Direction>(Error.class);
	public static final EnumMap<Error, Pattern> PATTERN = new EnumMap<Error, Pattern>(Error.class);
	
	
	static {
		
		NAME.put(UNKNOWN_ERROR,        "ERROR 0");
		NAME.put(COMMAND_UNSUPPORTED,  "ERROR 1");
		NAME.put(COMMAND_INVALID,      "ERROR 2");
		NAME.put(NAME_UNAVAILABLE,     "ERROR 3");
		NAME.put(FORBIDDEN,            "ERROR 4");
		NAME.put(ILLEGAL_MOVE,         "ERROR 5");
		NAME.put(SERVER_SHUTTING_DOWN, "ERROR 6");
		
		DIRECTION.put(UNKNOWN_ERROR,        Command.Direction.BIDIRECTIONAL);
		DIRECTION.put(COMMAND_UNSUPPORTED,  Command.Direction.BIDIRECTIONAL);
		DIRECTION.put(COMMAND_INVALID,      Command.Direction.BIDIRECTIONAL);
		DIRECTION.put(NAME_UNAVAILABLE,     Command.Direction.SERVER_TO_CLIENT);
		DIRECTION.put(FORBIDDEN,            Command.Direction.BIDIRECTIONAL);
		DIRECTION.put(ILLEGAL_MOVE,         Command.Direction.BIDIRECTIONAL);
		DIRECTION.put(SERVER_SHUTTING_DOWN, Command.Direction.SERVER_TO_CLIENT);
		
		PATTERN.put(UNKNOWN_ERROR,        Pattern.compile("ERROR 0( \\S+)*"));
		PATTERN.put(COMMAND_UNSUPPORTED,  Pattern.compile("ERROR 1( \\S+)*"));
		PATTERN.put(COMMAND_INVALID,      Pattern.compile("ERROR 2( \\S+)*"));
		PATTERN.put(NAME_UNAVAILABLE,     Pattern.compile("ERROR 3( \\S+)*"));
		PATTERN.put(FORBIDDEN,            Pattern.compile("ERROR 4( \\S+)*"));
		PATTERN.put(ILLEGAL_MOVE,         Pattern.compile("ERROR 5( \\S+)*"));
		PATTERN.put(SERVER_SHUTTING_DOWN, Pattern.compile("ERROR 6( \\S+)*"));

	}
	
}
