package protocol.command;

import java.util.EnumMap;
import java.util.regex.Pattern;

import com.google.common.collect.EnumHashBiMap;

import util.exception.*;



public enum Acknowledgement implements Command {

	OK,
	SAY,
	LIST,
	LEADERBOARD;
	
	
	public String toString() {
		return NAME.get(this);
	}
	
	
	public static Acknowledgement parse(String[] command) throws CommandUnsupportedException {
		if (command != null && command.length > 0) {
			Acknowledgement result = NAME.inverse().get(command[0]);
			if (result != null) {
				return result;
			}
		}
		throw new CommandUnsupportedException();
	}
	
	
	public static boolean isValid(Acknowledgement a, String s, Command.Direction d) throws CommandForbiddenException, CommandInvalidException {
		if (d != Command.Direction.BIDIRECTIONAL && (DIRECTION.get(a) == d || DIRECTION.get(a) == Command.Direction.BIDIRECTIONAL)) {
			if (PATTERN.get(a).matcher(s).matches()) {
				return true;
			}
			throw new CommandInvalidException();
		}
		throw new CommandForbiddenException();
	}
	
	
	public static final EnumHashBiMap<Acknowledgement, String> NAME = EnumHashBiMap.create(Acknowledgement.class);
	public static final EnumMap<Acknowledgement, Command.Direction> DIRECTION = new EnumMap<Acknowledgement, Command.Direction>(Acknowledgement.class);
	public static final EnumMap<Acknowledgement, Pattern> PATTERN = new EnumMap<Acknowledgement, Pattern>(Acknowledgement.class);
	
	
	static {
		
		NAME.put(OK,          "OK" );
		NAME.put(SAY,         "SAY");
		NAME.put(LIST,        "LIST");
		NAME.put(LEADERBOARD, "LEADEROARD");
		
		DIRECTION.put(OK,          Command.Direction.BIDIRECTIONAL);
		DIRECTION.put(SAY,         Command.Direction.SERVER_TO_CLIENT);
		DIRECTION.put(LIST,        Command.Direction.SERVER_TO_CLIENT);
		DIRECTION.put(LEADERBOARD, Command.Direction.SERVER_TO_CLIENT);
		
		PATTERN.put(OK,          Pattern.compile("^OK( \\S+)*$"));
		PATTERN.put(SAY,         Pattern.compile("^SAY [A-Za-z0-9]{1-20}( \\S+)+$"));
		PATTERN.put(LIST,        Pattern.compile("^LIST( [A-Za-z0-9]{1-20}:(READY|UNREADY))*$"));
		PATTERN.put(LEADERBOARD, Pattern.compile("^LEADERBOARD( [A-Za-z0-9]{1-20}:-?\\d+)*$"));
		
	}
	
}