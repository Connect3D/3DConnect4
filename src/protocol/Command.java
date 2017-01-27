package protocol;

import java.util.Arrays;
import util.*;
import util.exception.*;



public interface Command {
	
	public static Pair<Command, String[]> parse(String in, boolean serverPerspective) throws CommandException {
		
		String[] command = Util.arrayTrim(in.split(" "));
		
		// TODO throw command unsupported exception
		
		if (command.length > 0) {
			
			Acknowledgement ack  = Acknowledgement.parse(command[0]);
			Action action = Action.parse(command[0]);
			
			// return proper ERROR and arguments, or unknown command error
			if (ack != null && ack == Acknowledgement.ERROR) {
				if (command.length > 1) {
					Error error = Error.parse(command[0] + " " + command[1]);
					if (error != null) {
						return new Pair<Command, String[]> (error, Arrays.copyOfRange(command, 2, command.length));
					}
				}
				throw new UnknownCommandException();
			}
			
			// We have an Error code, return proper error code and arguments, or unknown command error
			if (action != null && action == Action.EXIT) {
				if (command.length > 1) {
					Exit exit = Exit.parse(command[0] + " " + command[1]);
					if (exit != null) {
						return new Pair<Command, String[]> (exit, Arrays.copyOfRange(command, 2, command.length));
					}
				}
				throw new UnknownCommandException();
			}
			
			// we have some command that could be either action or acknowledgement
			// we find out which first, depending on perspective, and then return proper command and arguments
			if (ack != null && ack == Acknowledgement.LIST || ack == Acknowledgement.LEADERBOARD) {
				if (serverPerspective) {
					return new Pair<Command, String[]> (action, Arrays.copyOfRange(command, 1, command.length));
				}
				else {
					return new Pair<Command, String[]> (ack, Arrays.copyOfRange(command, 1, command.length));
				}
			}
			
			// we did have an acknowledgement but it was a regular one
			if (ack != null ){
				return new Pair<Command, String[]>(ack, Arrays.copyOfRange(command, 1, command.length)); 
			}
			
			// we have some action, but it was a regular one
			if (action != null) {
				return new Pair<Command, String[]>(action, Arrays.copyOfRange(command, 1, command.length));
			}
		}
		
		throw new CommandInvalidException();
	}
	/*
	// TODO place here and necessary?
	public static boolean syntacticallyCorrect() {
		// TODO return true if command is either valid acknowledgement or action
		
		return true;
	}*/
	
	/*
	public static AcknowledgementCode fromString(String[] input) {
		return null;
		//return fromString(input[0]);
	}
	
	
	// make in constructor and make private varible that holds error code
	public static Acknowledgement fromString(String input) {
		for (AcknowledgementCode ack : AcknowledgementCode.values()) {
			if (ack.toString().equals(input)) {
				return ack;
			}
		}
		return null;
	}*/
	/*
	
	public String toString() {
		String[] arr = acknowledgements.get(this);
		return arr.length == 1 ? arr[0] : arr[0] + " " + arr[1];
	}
	*/
	/*
	 * 
	 * 
	 * 
	public static final String[] validCommands = { 
		"OK", 
		"ERROR",
		"CONNECT",
		"DISCONNECT",
		"READY",
		"UNREADY",
		"START",
		"MOVE",
		"EXIT",
		"SAY",
		"AVAILABLE",
		"LIST",
		"LEADERBOARD",
		"CHALLENGE",
		"ACCEPT",
		"DECLINE"
	};
	

	// TODO uncomment commands we will be supporting
	public static final String[] supportedCommands = {
		"OK",
		"ERROR",
		"CONNECT",
		"DISCONNECT",
		"READY",
		"UNREADY",
		"START",
		"MOVE",
		"EXIT",
		"SAY",
		"AVAILABLE",
		//"LIST",
		//"LEADERBOARD",
		//"CHALLENGE",
		//"ACCEPT",
		//"DECLINE"
	};
	
	
	public static boolean syntacticallyCorrect(String command) {
		return syntacticallyCorrect(command.split(" "));
	}
	
	
	public static boolean syntacticallyCorrect(String[] command) {
		if (command.length > 0 && Util.contains(validCommands, command[0])) {
			switch (command[0]) {
				case "OK": return true;								// always valid, cause unknown length
				case "ERROR": return command.length >= 2 && Util.isInt(command[1]);		// >= because error messages can return more info
				case "CONNECT": return command.length == 2;
				case "DISCONNECT": return command.length == 1;
				case "READY": return command.length == 1;
				case "UNREADY": return command.length == 1;
				case "START": return command.length == 3;
				case "MOVE": return command.length == 3 && Util.isInt(command[1]) && Util.isInt(command[2]);
				case "EXIT": return command.length == 2;
				case "SAY": return command.length >= 2;				// unknown length
				case "AVAILABLE": return command.length == 1;
				case "LIST": return true;							// always valid cause length is unknown
				case "LEADERBOARD": return true;					// same as previous
				case "CHALLENGE": return command.length == 2;
				case "ACCEPT": return command.length == 2;
				case "DECLINE": return command.length == 2;
			}
		}
		return false;
	}
	
	
	public static boolean commandSupported(String arg) {
		return Util.contains(supportedCommands, arg);
	}
	 */
	
}
