package game.player;


public enum PlayerType {
	
	HUMAN, ONLINE, RANDOM, MINIMAX;
	
	
	public static PlayerType fromString(String in) {
		if (in.equals("-h") || in.equals("-H")) { 
			return HUMAN;
		}
		if (in.equals("-o") || in.equals("-o")) {
			return ONLINE;
		}
		if (in.equals("-r") || in.equals("-r")) {
			return RANDOM;
		}
		if (in.equals("-m") || in.equals("-m")) {
			return MINIMAX;
		}
		return null;
	}
	
}
