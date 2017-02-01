package util.test;

import static org.junit.Assert.*;

import org.junit.Test;

import protocol.CommandParser;
import protocol.command.Action;
import protocol.command.Command;
import util.container.Pair;
import util.exception.CommandForbiddenException;
import util.exception.CommandInvalidException;
import util.exception.CommandUnsupportedException;


public class Testtest {

	CommandParser parserCS = new CommandParser(Command.Direction.CLIENT_TO_SERVER);
	CommandParser parserSC = new CommandParser(Command.Direction.SERVER_TO_CLIENT);
	
	/*
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
	*/
	
	@Test
	public void testActionClientServer() throws 
		CommandUnsupportedException, 
		CommandForbiddenException, 
		CommandInvalidException {
		
		String action1 = "MOVE 2 3";
		String action2 = "CONNECT hallo";
		String action3 = "UNREADY";
		String action4 = "SAY say test";
		String action5 = "AVAILABLE";
		
		Pair<Command, String[]> result1 = parserCS.parse(action1);
		Pair<Command, String[]> result2 = parserCS.parse(action2);
		Pair<Command, String[]> result3 = parserCS.parse(action3);
		Pair<Command, String[]> result4 = parserCS.parse(action4);
		Pair<Command, String[]> result5 = parserCS.parse(action5);
		
		assertEquals(result1.first, Action.MOVE);
		assertEquals(result2.first, Action.CONNECT);
		assertEquals(result3.first, Action.UNREADY);
		assertEquals(result4.first, Action.SAY);
		assertEquals(result5.first, Action.AVAILABLE);
		
		assertEquals(result1.second[0], "2");
		assertEquals(result1.second[1], "3");
		assertEquals(result2.second[0], "hallo");
		assertEquals(result4.second[0], "say");
		assertEquals(result4.second[1], "test");
		
	}
	
	
	@Test
	public void testActionServerClient() throws 
		CommandUnsupportedException, 
		CommandForbiddenException, 
		CommandInvalidException {
		
		String action1 = "CHALLENGE persoon";
		String action2 = "START 1 2";
		String action3 = "DISCONNECT";
		
		Pair<Command, String[]> result1 = parserSC.parse(action1);
		Pair<Command, String[]> result2 = parserSC.parse(action2);
		Pair<Command, String[]> result3 = parserSC.parse(action3);
		
		assertEquals(result1.first, Action.CHALLENGE);
		assertEquals(result2.first, Action.START);
		assertEquals(result3.first, Action.DISCONNECT);
		
		assertEquals(result1.second[0], "persoon");
		assertEquals(result2.second[0], "1");
		assertEquals(result2.second[1], "2");
		
	}
	

}
