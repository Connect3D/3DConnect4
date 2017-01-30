package protocol.command;

import java.util.EnumMap;
import java.util.regex.Pattern;

import com.google.common.collect.EnumHashBiMap;

import util.exception.*;



public enum Action implements Command {
	
	CONNECT,
	DISCONNECT,
	READY,
	UNREADY,
	START,
	MOVE,
	SAY,
	AVAILABLE,
	LIST,
	LEADERBOARD,
	CHALLENGE,
	ACCEPT,
	DECLINE;
	
	
	public String toString() {
		return NAME.get(this);
	}
	
	
	public static Action parse(String[] command) throws CommandUnsupportedException {
		if (command != null && command.length > 0) {
			Action result = NAME.inverse().get(command[0]);
			if (result != null) {
				return result;
			}
		}
		throw new CommandUnsupportedException();
	}
	
	
	public static boolean isValid(Action a, String s, Command.Direction d) throws CommandForbiddenException, CommandInvalidException {
		if (d != Command.Direction.BIDIRECTIONAL && (DIRECTION.get(a) == d || DIRECTION.get(a) == Command.Direction.BIDIRECTIONAL)) {
			if (PATTERN.get(a).matcher(s).matches()) {
				return true;
			}
			throw new CommandInvalidException();
		}
		throw new CommandForbiddenException();
	}
	
	
	public static final EnumHashBiMap<Action, String> NAME = EnumHashBiMap.create(Action.class);
	public static final EnumMap<Action, Command.Direction> DIRECTION = new EnumMap<Action, Command.Direction>(Action.class);
	public static final EnumMap<Action, Pattern> PATTERN = new EnumMap<Action, Pattern>(Action.class);
	
	
	static {
		
		NAME.put(CONNECT,     "CONNECT");
		NAME.put(DISCONNECT,  "DISCONNECT");
		NAME.put(READY,       "READY");
		NAME.put(UNREADY,     "UNREADY");
		NAME.put(START,       "START");
		NAME.put(MOVE,        "MOVE");
		NAME.put(SAY,         "SAY");
		NAME.put(AVAILABLE,   "AVAILABLE");
		NAME.put(LIST,        "LIST");
		NAME.put(LEADERBOARD, "LEADERBOARD");
		NAME.put(CHALLENGE,   "CHALLENGE");
		NAME.put(ACCEPT,      "ACCEPT");
		NAME.put(DECLINE,     "DECLINE");
		
		DIRECTION.put(CONNECT,     Command.Direction.CLIENT_TO_SERVER);
		DIRECTION.put(DISCONNECT,  Command.Direction.BIDIRECTIONAL);
		DIRECTION.put(READY,       Command.Direction.CLIENT_TO_SERVER);
		DIRECTION.put(UNREADY,     Command.Direction.CLIENT_TO_SERVER);
		DIRECTION.put(START,       Command.Direction.SERVER_TO_CLIENT);
		DIRECTION.put(MOVE,        Command.Direction.BIDIRECTIONAL);
		DIRECTION.put(SAY,         Command.Direction.CLIENT_TO_SERVER);
		DIRECTION.put(AVAILABLE,   Command.Direction.CLIENT_TO_SERVER);
		DIRECTION.put(LIST,        Command.Direction.CLIENT_TO_SERVER);
		DIRECTION.put(LEADERBOARD, Command.Direction.CLIENT_TO_SERVER);
		DIRECTION.put(CHALLENGE,   Command.Direction.BIDIRECTIONAL);
		DIRECTION.put(ACCEPT,      Command.Direction.CLIENT_TO_SERVER);
		DIRECTION.put(DECLINE,     Command.Direction.CLIENT_TO_SERVER);
		
		PATTERN.put(CONNECT,     Pattern.compile("^CONNECT [A-Za-z0-9]{1-20}$"));
		PATTERN.put(DISCONNECT,  Pattern.compile("^DISCONNECT$"));
		PATTERN.put(READY,       Pattern.compile("^READY$"));
		PATTERN.put(UNREADY,     Pattern.compile("^UNREADY$"));
		PATTERN.put(START,       Pattern.compile("START [A-Za-z0-9]{1-20} [A-Za-z0-9]{1-20}$"));
		PATTERN.put(MOVE,        Pattern.compile("^MOVE [0-3] [0-3]$"));
		PATTERN.put(SAY,         Pattern.compile("^SAY( \\S+)+$"));
		PATTERN.put(AVAILABLE,   Pattern.compile("^AVAILABLE$"));
		PATTERN.put(LIST,        Pattern.compile("^LIST$"));
		PATTERN.put(LEADERBOARD, Pattern.compile("^LEADERBOARD$"));
		PATTERN.put(CHALLENGE,   Pattern.compile("^CHALLENGE [A-Za-z0-9]{1-20}$"));
		PATTERN.put(ACCEPT,      Pattern.compile("^ACCEPT [A-Za-z0-9]{1-20}$"));
		PATTERN.put(DECLINE,     Pattern.compile("^DECLINE [A-Za-z0-9]{1-20}$"));
		
	}
	
}
