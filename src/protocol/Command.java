package protocol;

import java.util.Arrays;
import util.*;
import util.exception.*;



public interface Command {
	
	public static Pair<Command, String[]> parse(String in, boolean serverPerspective) throws CommandException {
		
		String[] command = Util.arrayTrim(in.split(" "));
		
		if (command.length > 0) {
			
			Acknowledgement acknowledgement = Acknowledgement.parse(command[0]);
			Action action = Action.parse(command[0]);
			
			// if server calls function, and does not support the command, throw error
			if (serverPerspective && (!serverSupports(acknowledgement) || !serverSupports(action))) {
				throw new CommandUnsupportedException();
			}
			
			// if client calls function, and does not support the command, throw error
			if (!serverPerspective && (!clientSupports(acknowledgement) || !clientSupports(action))) {
				throw new CommandUnsupportedException();
			}
			
			// if the command was an Error message return correct error message
			if (acknowledgement != null && acknowledgement == Acknowledgement.ERROR) {
				if (command.length > 1) {
					Error error = Error.parse(command[0] + " " + command[1]);
					if (error != null) {
						return new Pair<Command, String[]> (error, Arrays.copyOfRange(command, 2, command.length));
					}
				}
				throw new UnknownCommandException();
			}
			
			//if the command was an exit code return the proper exit code
			if (action != null && action == Action.EXIT) {
				if (command.length > 1) {
					Exit exit = Exit.parse(command[0] + " " + command[1]);
					if (exit != null) {
						return new Pair<Command, String[]> (exit, Arrays.copyOfRange(command, 2, command.length));
					}
				}
				throw new UnknownCommandException();
			}
			
			// we have a command that could be either an Action or an Acknowledgement, can be differentiated by looking at where call came from
			if (acknowledgement != null && acknowledgement == Acknowledgement.LIST || acknowledgement == Acknowledgement.LEADERBOARD) {
				if (serverPerspective) {
					return new Pair<Command, String[]> (action, Arrays.copyOfRange(command, 1, command.length));
				}
				else {
					return new Pair<Command, String[]> (acknowledgement, Arrays.copyOfRange(command, 1, command.length));
				}
			}
			
			// we did have an acknowledgement but it was a regular one (only OK)
			if (acknowledgement != null ){
				return new Pair<Command, String[]>(acknowledgement, Arrays.copyOfRange(command, 1, command.length)); 
			}
			
			// we have some action, but it was a regular one
			if (action != null) {
				return new Pair<Command, String[]>(action, Arrays.copyOfRange(command, 1, command.length));
			}
		}
		
		throw new CommandInvalidException();
	}
	

	// TODO Richard confirm/ update when necessary
	public static boolean clientSupports(Command command) {
		return  command != Action.CHALLENGE &&
				command != Action.ACCEPT &&
				command != Action.DECLINE &&
				command != Action.LIST &&
				command != Action.LEADERBOARD &&
				command != Acknowledgement.LIST &&
				command != Acknowledgement.LEADERBOARD;
	}
	
	
	public static boolean serverSupports(Command command) {
		return  command != Action.CHALLENGE &&
				command != Action.ACCEPT &&
				command != Action.DECLINE &&
				command != Action.LIST &&
				command != Action.LEADERBOARD &&
				command != Acknowledgement.LIST &&
				command != Acknowledgement.LEADERBOARD;
	}
	
}
