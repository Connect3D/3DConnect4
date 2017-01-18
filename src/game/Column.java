package game;

import exception.InvalidColumnException;

public class Column {

	public final int x;
	public final int y;
	
	
	public Column(int _x, int _y) {
		if (_x < 0 || _x >= Board.WIDTH) throw new InvalidColumnException(_x, _y);
		if (_y < 0 || _y >= Board.DEPTH) throw new InvalidColumnException(_x, _y);
		x = _x;
		y = _y;
	}
	
}
