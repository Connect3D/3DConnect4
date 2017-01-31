package protocol.command;

import java.util.EnumMap;
import java.util.regex.Pattern;

import com.google.common.collect.EnumHashBiMap;

import util.exception.*;
import util.exception.protocol.CommandForbiddenException;
import util.exception.protocol.CommandInvalidException;
import util.exception.protocol.CommandUnsupportedException;



public enum Exit implements Command {

	WON,
	LOST,
	DRAW,
	TIMEOUT,
	FORFEITURE;
	
	
	public String toString() {
		return NAME.get(this);
	}
	
	
	public static Exit parse(String[] command) throws CommandUnsupportedException, CommandInvalidException {
		if (command != null && command.length > 1) {
			if (command[0].equals("EXIT")) {
				Exit result = NAME.inverse().get(command[0] + " " + command[1]);
				if (result != null) {
					return result;
				}
			}
			throw new CommandInvalidException();
		}
		throw new CommandUnsupportedException();
	}
	
	
	public static boolean isValid(Exit e, String s, Command.Direction d) throws CommandForbiddenException, CommandInvalidException {
		if (d != Command.Direction.BIDIRECTIONAL && (DIRECTION.get(e) == d || DIRECTION.get(e) == Command.Direction.BIDIRECTIONAL)) {
			if (PATTERN.get(e).matcher("\n" + s + "\n").matches()) {
				return true;
			}
			throw new CommandInvalidException();
		}
		throw new CommandForbiddenException();
	}
	
	
	public static final EnumHashBiMap<Exit, String> NAME = EnumHashBiMap.create(Exit.class);
	public static final EnumMap<Exit, Command.Direction> DIRECTION = new EnumMap<Exit, Command.Direction>(Exit.class);
	public static final EnumMap<Exit, Pattern> PATTERN = new EnumMap<Exit, Pattern>(Exit.class);
	
	
	static {
		
		NAME.put(WON,        "EXIT WON");
		NAME.put(LOST,       "EXIT LOST");
		NAME.put(DRAW,       "EXIT DRAW");
		NAME.put(TIMEOUT,    "EXIT TIMEOUT");
		NAME.put(FORFEITURE, "EXIT FORFEITURE");
		
		DIRECTION.put(WON,        Command.Direction.SERVER_TO_CLIENT);
		DIRECTION.put(LOST,       Command.Direction.SERVER_TO_CLIENT);
		DIRECTION.put(DRAW,       Command.Direction.SERVER_TO_CLIENT);
		DIRECTION.put(TIMEOUT,    Command.Direction.SERVER_TO_CLIENT);
		DIRECTION.put(FORFEITURE, Command.Direction.BIDIRECTIONAL);
		
		PATTERN.put(WON,        Pattern.compile("EXIT WON"));
		PATTERN.put(LOST,       Pattern.compile("EXIT LOST"));
		PATTERN.put(DRAW,       Pattern.compile("EXIT DRAW"));
		PATTERN.put(TIMEOUT,    Pattern.compile("EXIT TIMEOUT"));
		PATTERN.put(FORFEITURE, Pattern.compile("EXIT FORFEITURE"));
		
	}
	
}
