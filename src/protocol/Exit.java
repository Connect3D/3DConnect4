package protocol;

import com.google.common.collect.EnumHashBiMap;



public enum Exit implements Command {

	WON,
	LOST,
	DRAW,
	TIMEOUT,
	FORFEITURE;
	
	
	public String toString() {
		return EXITS.get(this);
	}
	
	
	public String[] toStringArray() {
		return EXITS.get(this).split(" ");
	}
	
	
	protected static Exit parse(String s) {
		return EXITS.inverse().get(s);
	}
	
	
	protected static final EnumHashBiMap<Exit, String> EXITS = EnumHashBiMap.create(Exit.class);
	
	static {
		EXITS.put(WON, "EXIT WON");
		EXITS.put(LOST, "EXIT LOST");
		EXITS.put(DRAW, "EXIT DRAW");
		EXITS.put(TIMEOUT, "EXIT TIMEOUT");
		EXITS.put(FORFEITURE, "EXIT FORFEITURE");
	}
	
}
