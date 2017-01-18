package game;

public class Game {

	public static final int CONSECUTIVE_MARKS_TO_WIN = 4;
	
	public enum Ending {
		
		NOT_ENDED, DRAW, X_WINS, O_WINS;
		
		// way to pass by value (enums and classes and thus normally passed by reference)
		public Ending copy() {
			if (this == NOT_ENDED) return NOT_ENDED;
			if (this == DRAW) return DRAW;
			if (this == X_WINS) return X_WINS;
			else return O_WINS;
		}
		
	}
	
	
	public static void main(String[] args) {
		
	}

}
