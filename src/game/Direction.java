package game;


// no access specifier for package scope
class Direction {
	
	public static final Direction FIRST = new Direction(DirectionX.LEFT, DirectionY.BACKWARD, DirectionZ.DOWN);
	public static final Direction LAST = new Direction(DirectionX.RIGHT, DirectionY.FORWARD, DirectionZ.UP);
	public static final Direction MIDDLE = new Direction(DirectionX.CENTER, DirectionY.CENTER, DirectionZ.CENTER);
	
	public final DirectionX x;
	public final DirectionY y;
	public final DirectionZ z;
	
	public Direction(DirectionX _x, DirectionY _y, DirectionZ _z) {
		x = _x;
		y = _y;
		z = _z;
	}
	
	public Direction opposite() {
		return new Direction(this.x.opposite(), this.y.opposite(), this.z.opposite());
	}
	
	public Direction next() {
		if (z != DirectionZ.UP) return new Direction(x, y, z.increment());
		if (y != DirectionY.FORWARD) return new Direction(x, y.increment(), z.increment());
		if (x != DirectionX.RIGHT) return new Direction(x.increment(), y.increment(), z.increment());
		else return null;
	}
	
	public boolean equals(Direction other) {
		return x == other.x && y == other.y && z == other.z;
	}
	
	public Position asRelativePosition() {
		return new Position(x.toInt(), y.toInt(), z.toInt());
	}
	
}


//no access specifier for package scope
enum DirectionX {
	
	LEFT, CENTER, RIGHT;
	
	public DirectionX opposite() {
		if (this == LEFT) return RIGHT;
		if (this == RIGHT) return LEFT;
		else return CENTER;
	}
	
	public int toInt() {
		if (this == LEFT) return -1;
		if (this == RIGHT) return 1;
		else return 0;
	}
	
	public DirectionX increment() {
		if (this == LEFT) return CENTER;
		if (this == CENTER) return RIGHT;
		else return LEFT;
	}
	
}


//no access specifier for package scope
enum DirectionY {
	
	BACKWARD, CENTER, FORWARD;
	
	public DirectionY opposite() {
		if (this == BACKWARD) return FORWARD;
		if (this == FORWARD) return BACKWARD;
		else return CENTER;
	}
	
	public int toInt() {
		if (this == BACKWARD) return -1;
		if (this == FORWARD) return 1;
		else return 0;
	}
	
	public DirectionY increment() {
		if (this == BACKWARD) return CENTER;
		if (this == CENTER) return FORWARD;
		else return BACKWARD;
	}
	
}


//no access specifier for package scope
enum DirectionZ {
	
	DOWN, CENTER, UP;
	
	public DirectionZ opposite() {
		if (this == DOWN) return UP;
		if (this == UP) return DOWN;
		else return CENTER;
	}
	
	public int toInt() {
		if (this == DOWN) return -1;
		if (this == UP) return 1;
		else return 0;
	}
	
	public DirectionZ increment() {
		if (this == DOWN) return CENTER;
		if (this == CENTER) return UP;
		else return DOWN;
	}
	
}
