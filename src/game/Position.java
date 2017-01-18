package game;

import exception.InvalidPositionException;


// TODO: decide whether we should give public or package scope
public class Position {
	
	public final int x;
	public  final int y;
	public final int z;
	
	
	public Position(int _x, int _y, int _z) {
		if (!isValid(_x, _y, _z)) {
			throw new InvalidPositionException(_x, _y, _z);
		}
		x = _x;
		y = _y;
		z = _z;
	}
	
	
	public static boolean isValid(int x, int y, int z) {
		return x >= 0 && x < Board.WIDTH && y >= 0 && y < Board.DEPTH && z >= 0 && z < Board.HEIGHT;
	}
	
	
	// TODO check correct usage of WIDTH and DEPTH on non 4*4*4 board
	public static int indexOf(int x, int y, int z) {
		if (!isValid(x, y, z)) {
			throw new InvalidPositionException(x, y, z);
		}
		return x * Board.WIDTH * Board.WIDTH + y * Board.DEPTH + z;
	}
	
	
	public int index() {
		return indexOf(x, y, z);
	}
	
	
	// returns null if new position is invalid
	public Position inDirection(Direction direction) {
		if (isValid(x + direction.x_axis.toInt(), y + direction.y_axis.toInt(), z + direction.z_axis.toInt())) {
			return new Position(x + direction.x_axis.toInt(), y + direction.y_axis.toInt(), z + direction.z_axis.toInt());
		}
		return null;
	}
	
}
