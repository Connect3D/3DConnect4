package protocol;

import com.google.common.collect.EnumHashBiMap;



public enum Acknowledgement implements Command {

	OK,
	ERROR,
	LIST,
	LEADERBOARD;
	
	
	public String toString() {
		return ACKNOWLEDGEMENTS.get(this);
	}
	
	
	protected static Acknowledgement parse(String s) {
		return ACKNOWLEDGEMENTS.inverse().get(s);
	}
	
	
	protected static final EnumHashBiMap<Acknowledgement, String> ACKNOWLEDGEMENTS = EnumHashBiMap.create(Acknowledgement.class);
	
	static {
		ACKNOWLEDGEMENTS.put(OK,          "OK" );
		ACKNOWLEDGEMENTS.put(ERROR,       "ERROR");
		ACKNOWLEDGEMENTS.put(LIST,        "LIST");
		ACKNOWLEDGEMENTS.put(LEADERBOARD, "LEADEROARD");
	}
	
}
