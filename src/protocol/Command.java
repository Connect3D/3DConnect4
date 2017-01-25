package protocol;

import util.Util;

public class Command {

	// TODO add chat command!
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
		"LIST",
		"CHALLENGE",
		"ACCEPT",
		"DECLINE"
	};

	// TODO add chat command!
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
		//"LIST",
		//"CHALLENGE",
		//"ACCEPT",
		//"DECLINE"
	};
	
	
	// TODO add chat command!
	public static boolean syntacticallyCorrect(String arg) {
		String[] command = arg.split(" ");
		if (command.length > 0 && Util.contains(validCommands, command[0])) {
			switch (command[0]) {
				case "OK": return true;	// always syntactically correct when the command starts with ok.
				case "ERROR": return command.length >= 2 && Util.isInt(command[1]);
				case "CONNECT": return command.length == 2;
				case "DISCONNECT": return command.length == 1;
				case "READY": return command.length == 1;
				case "UNREADY": return command.length == 1;
				case "START": return command.length == 3;
				case "MOVE": return command.length == 3 && Util.isInt(command[1]) && Util.isInt(command[2]);
				case "EXIT": return command.length == 2;
				case "LIST": return true;	// always valid cause length is unknown
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
	
	
}
