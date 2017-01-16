package game;


public enum Mark {
	
	EMPTY, X, O;
	
	public Mark opposite() {
		if (this == X) return O;
		if (this == O) return X;
		else return EMPTY;
	}
	
	public String toString() {
		if (this == X) return "X";
		if (this == O) return "O";
		else return " ";
	}
	
}
