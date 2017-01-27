package protocol;

import java.util.Arrays;
import util.*;
import util.exception.*;



public interface Command {
	
	
	public static Pair<Command, String[]> parse(String in, boolean serverPerspective) throws CommandInvalidException, CommandUnsupportedException {
		
		String[] command = Util.arrayTrim(in.split(" "));
		
		if (command.length > 0) {
			
			Acknowledgement acknowledgement = Acknowledgement.parse(command[0]);
			Action action = Action.parse(command[0]);
			
			if (serverPerspective && (!serverSupports(acknowledgement) || !serverSupports(action))) throw new CommandUnsupportedException();
			if (!serverPerspective && (!clientSupports(acknowledgement) || !clientSupports(action))) throw new CommandUnsupportedException();
			
			
			if (acknowledgement != null && action != null) {
				if (serverPerspective && correctSize(action, command.length)) {
					return new Pair<Command, String[]> (action, Arrays.copyOfRange(command, 1, command.length));
				}
				else if (!serverPerspective && correctSize(acknowledgement, command.length)) {
					return new Pair<Command, String[]>(acknowledgement, Arrays.copyOfRange(command, 1, command.length));
				}
			}
			
			else if (acknowledgement != null) {
				if (correctSize(acknowledgement, command.length)) {
					if (acknowledgement == Acknowledgement.ERROR) {
						Error error = Error.parse(command[0] + " " + command[1]);
						if (error != null) {
							return new Pair<Command, String[]> (error, Arrays.copyOfRange(command, 2, command.length));
						}
					}
					else {
						return new Pair<Command, String[]>(acknowledgement, Arrays.copyOfRange(command, 1, command.length));
					}
				}
			}
			
			else if (action != null) {
				if (correctSize(action, command.length)) {
					if (action == Action.EXIT) {
						Exit exit = Exit.parse(command[0] + " " + command[1]);
						if (exit != null) {
							return new Pair<Command, String[]> (exit, Arrays.copyOfRange(command, 2, command.length));
						}
					}
					else {
						return new Pair<Command, String[]>(action, Arrays.copyOfRange(command, 1, command.length));
					}
				}
			}
		}
		
		throw new CommandInvalidException();
	}

	
	// TODO Richard: confirm/ update when necessary
	public static boolean clientSupports(Command command) {
		if (command == null) return true;
		return  command != Action.CHALLENGE &&
				command != Action.ACCEPT &&
				command != Action.DECLINE &&
				command != Action.LIST &&
				command != Action.LEADERBOARD &&
				command != Acknowledgement.LIST &&
				command != Acknowledgement.LEADERBOARD;
	}
	
	
	public static boolean serverSupports(Command command) {
		if (command == null) return true;
		return  command != Action.CHALLENGE &&
				command != Action.ACCEPT &&
				command != Action.DECLINE &&
				command != Action.LIST &&
				command != Action.LEADERBOARD &&
				command != Acknowledgement.LIST &&
				command != Acknowledgement.LEADERBOARD;
	}
	
	
	public static boolean correctSize(Acknowledgement a, int i) {
		return Acknowledgement.ARG_SIZE.get(a).satisfies(i);
	}
	
	
	public static boolean correctSize(Action a, int i) {
		return Action.ARG_SIZE.get(a).satisfies(i);
	}
	
}
