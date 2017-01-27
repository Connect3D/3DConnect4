package protocol;

import java.util.EnumMap;

import com.google.common.collect.EnumHashBiMap;

import util.Amount;



public enum Acknowledgement implements Command {

	OK,
	ERROR,
	LIST,
	LEADERBOARD;
	
	
	public String toString() {
		return ACKNOWLEDGEMENTS.get(this);
	}
	
	
	// use the parse function of Command outside of package
	protected static Acknowledgement parse(String s) {
		return ACKNOWLEDGEMENTS.inverse().get(s);
	}
	
	
	protected static final EnumHashBiMap<Acknowledgement, String> ACKNOWLEDGEMENTS = EnumHashBiMap.create(Acknowledgement.class);
	protected static final EnumMap<Acknowledgement, Amount> ARG_SIZE = new EnumMap<Acknowledgement, Amount>(Acknowledgement.class);
	
	static {
		ACKNOWLEDGEMENTS.put(OK,          "OK" );
		ACKNOWLEDGEMENTS.put(ERROR,       "ERROR");
		ACKNOWLEDGEMENTS.put(LIST,        "LIST");
		ACKNOWLEDGEMENTS.put(LEADERBOARD, "LEADEROARD");
		ARG_SIZE.put(OK,          new Amount.Atleast(1));
		ARG_SIZE.put(ERROR,       new Amount.Atleast(2));
		ARG_SIZE.put(LIST,        new Amount.Atleast(1));
		ARG_SIZE.put(LEADERBOARD, new Amount.Atleast(1));
	}
	
}
