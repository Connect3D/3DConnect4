package protocol;

import util.Util;

public class Command {


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
				case "SAY": return command.length >= 3;
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
	
	
}
