package protocol;

import java.util.EnumMap;

import com.google.common.collect.EnumHashBiMap;

import util.Amount;



public enum Action implements Command {
	
	CONNECT,
	DISCONNECT,			// unacknowledged
	READY,
	UNREADY,
	START,
	MOVE,
	EXIT,
	SAY,				// unacknowledged
	AVAILABLE,
	LIST,
	LEADERBOARD,
	CHALLENGE,
	ACCEPT,
	DECLINE;
	
	
	public String toString() {
		return ACTIONS.get(this);
	}
	
	
	// use the parse function of Command outside of package
	protected static Action parse(String s) {
		return ACTIONS.inverse().get(s);
	}
	
	
	protected static final EnumHashBiMap<Action, String> ACTIONS = EnumHashBiMap.create(Action.class);
	protected static final EnumMap<Action, Amount> ARG_SIZE = new EnumMap<Action, Amount>(Action.class);
	
	static {
		ACTIONS.put(CONNECT,     "CONNECT");
		ACTIONS.put(DISCONNECT,  "DISCONNECT");
		ACTIONS.put(READY,       "READY");
		ACTIONS.put(UNREADY,     "UNREADY");
		ACTIONS.put(START,       "START");
		ACTIONS.put(MOVE,        "MOVE");
		ACTIONS.put(EXIT,        "EXIT");
		ACTIONS.put(SAY,         "SAY");
		ACTIONS.put(AVAILABLE,   "AVAILABLE");
		ACTIONS.put(LIST,        "LIST");
		ACTIONS.put(LEADERBOARD, "LEADERBOARD");
		ACTIONS.put(CHALLENGE,   "CHALLENGE");
		ACTIONS.put(ACCEPT,      "ACCEPT");
		ACTIONS.put(DECLINE,     "DECLINE");
		ARG_SIZE.put(CONNECT,     new Amount.Exact(2));
		ARG_SIZE.put(DISCONNECT,  new Amount.Exact(1));
		ARG_SIZE.put(READY,       new Amount.Exact(1));
		ARG_SIZE.put(UNREADY,     new Amount.Exact(1));
		ARG_SIZE.put(START,       new Amount.Exact(3));
		ARG_SIZE.put(MOVE,        new Amount.Exact(3));
		ARG_SIZE.put(EXIT,        new Amount.Exact(2));
		ARG_SIZE.put(SAY,         new Amount.Atleast(1));
		ARG_SIZE.put(AVAILABLE,   new Amount.Exact(1));
		ARG_SIZE.put(LIST,        new Amount.Exact(1));
		ARG_SIZE.put(LEADERBOARD, new Amount.Exact(1));
		ARG_SIZE.put(CHALLENGE,   new Amount.Exact(2));
		ARG_SIZE.put(ACCEPT,      new Amount.Exact(2));
		ARG_SIZE.put(DECLINE,     new Amount.Exact(2));
	}
	
}
