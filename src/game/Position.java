package game;

import util.exception.InvalidPositionException;



public class Position {
	
	public final int x;
	public final int y;
	public final int z;
	
	
	public Position(int x, int y, int z) {
		if (!isValid(x, y, z)) {
			throw new InvalidPositionException(x, y, z);
		}
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public static boolean isValid(int x, int y, int z) {
		return x >= 0 && x < Board.WIDTH && y >= 0 && y < Board.DEPTH && z >= 0 && z < Board.HEIGHT;
	}
	

	public static int indexOf(int x, int y, int z) {
		if (!isValid(x, y, z)) {
			throw new InvalidPositionException(x, y, z);
		}
		return x * Board.HEIGHT * Board.DEPTH + y * Board.HEIGHT + z;
	}
	
	
	public int index() {
		return indexOf(x, y, z);
	}
	
	
	// returns null if new position is invalid
	public Position inDirection(Direction direction) {
		if (isValid(x + direction.xaxis.toInt(), y + direction.yaxis.toInt(),
				z + direction.zaxis.toInt())) {
			return new Position(x + direction.xaxis.toInt(), y + direction.yaxis.toInt(),
					z + direction.zaxis.toInt());
		}
		return null;
	}
	
}
