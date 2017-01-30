package protocol;

import protocol.command.Acknowledgement;
import protocol.command.Action;
import protocol.command.Command;
import protocol.command.Error;
import protocol.command.Exit;
import util.Util;
import util.exception.*;



public class CommandParser {
	
	public final Command.Direction direction;
	
	
	public CommandParser(Command.Direction d) {
		direction = d;
	}
	
	
	public Command parse(String input) throws CommandUnsupportedException, CommandForbiddenException, CommandInvalidException {
		
		String[] splittedCommand = Util.split(input);
		String command = Util.join(splittedCommand);			// all whitespace is now reduced to single spaces
		
		Command.Type type = Command.getType(splittedCommand, direction);		// throws unsupported exception if not recognized
		
		switch (type) {
			case ACTION:
				Action action = Action.parse(splittedCommand);
				if (Action.isValid(action, command, direction)) return action;
				break;
			case ACKNOWLEDGEMENT:
				Acknowledgement acknowledgement = Acknowledgement.parse(splittedCommand);
				if (Acknowledgement.isValid(acknowledgement, command, direction)) return acknowledgement;
				break;
			case EXIT:
				Exit exit = Exit.parse(splittedCommand);
				if (Exit.isValid(exit, command, direction)) return exit;
				break;
			case ERROR:
				Error error = Error.parse(splittedCommand);
				if (Error.isValid(error, command, direction)) return error;
				break;
		}
		
		return null;
	}
	
}
