package game;


public class Column {

	public final int x;
	public final int y;
	
	public Column(int _x, int _y) {
		if (_x < 0 || _x >= Board.WIDTH) throw new IllegalArgumentException("Column out of range, x was: " + _x);
		if (_y < 0 || _y >= Board.DEPTH) throw new IllegalArgumentException("Column out of range, y was: " + _y);
		x = _x;
		y = _y;
	}
	
}
