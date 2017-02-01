package game;

import util.exception.InvalidColumnException;

public class Column {

	public final int x;
	public final int y;
	
	
	public Column(int x, int y) {
		if (x < 0 || x >= Board.WIDTH) {
			throw new InvalidColumnException(x, y);
		}
		if (y < 0 || y >= Board.DEPTH) {
			throw new InvalidColumnException(x, y);
		}
		this.x = x;
		this.y = y;
	}
	
	
	public Column(Column other) {
		x = other.x;
		y = other.y;
	}
	
	
	public boolean equals(Column other) {
		return x == other.x && y == other.y;
	}
	
	
	public static boolean isValid(int x, int y) {
		return x >= 0 && x < Board.WIDTH && y >= 0 && y < Board.DEPTH;
	}
	
}
