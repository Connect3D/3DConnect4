package game;

import exceptions.InvalidPositionException;

public class Position {

	private final int x;
	private final int y;
	private final int z;

	
	public Position(int _x, int _y, int _z) {
		if (_x < 0 || _x >= Board.WIDTH || _y < 0 || _y >= Board.DEPTH || _z < 0 || _z >= Board.HEIGHT) {
			throw new InvalidPositionException(_x, _y, _z);
		}
		x = _x;
		y = _y;
		z = _z;
	}

	
	public int index() {
		return indexOf(x, y, z);
	}
	
	
	// TODO check correct usage of WIDTH and DEPTH on non 4*4*4 board
	public static int indexOf(int x, int y, int z) {
		if (x < 0 || x >= Board.WIDTH || y < 0 || y >= Board.DEPTH || z < 0 || z >= Board.HEIGHT) {
			throw new InvalidPositionException(x, y, z);
		}
		return x * Board.WIDTH * Board.WIDTH + y * Board.DEPTH + z;
	}

}
