package protocol;

import com.google.common.collect.EnumHashBiMap;



public enum Action implements Command {
	
	CONNECT,			// unacknowledged
	DISCONNECT,
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
	
	
	protected static Action parse(String s) {
		return ACTIONS.inverse().get(s);
	}
	
	
	protected static final EnumHashBiMap<Action, String> ACTIONS = EnumHashBiMap.create(Action.class);
	
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
	}
	
}
