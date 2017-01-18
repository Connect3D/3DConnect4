package game;


// no access specifier for package scope
class Direction {
	
	public static final Direction FIRST = new Direction(DirectionX.LEFT, DirectionY.BACKWARD, DirectionZ.DOWN);
	public static final Direction LAST = new Direction(DirectionX.RIGHT, DirectionY.FORWARD, DirectionZ.UP);
	public static final Direction MIDDLE = new Direction(DirectionX.CENTER, DirectionY.CENTER, DirectionZ.CENTER);
	
	public final DirectionX x_axis;
	public final DirectionY y_axis;
	public final DirectionZ z_axis;
	
	public Direction(DirectionX x, DirectionY y, DirectionZ z) {
		x_axis = x;
		y_axis = y;
		z_axis = z;
	}
	
	public Direction opposite() {
		return new Direction(this.x_axis.opposite(), this.y_axis.opposite(), this.z_axis.opposite());
	}
	
	public Direction next() {
		if (z_axis != DirectionZ.UP) return new Direction(x_axis, y_axis, z_axis.increment());
		if (y_axis != DirectionY.FORWARD) return new Direction(x_axis, y_axis.increment(), z_axis.increment());
		if (x_axis != DirectionX.RIGHT) return new Direction(x_axis.increment(), y_axis.increment(), z_axis.increment());
		else return null;
	}
	
	public boolean equals(Direction other) {
		return x_axis == other.x_axis && y_axis == other.y_axis && z_axis == other.z_axis;
	}
	
	/*// gives error because relative positions can be negative, but board positions not
	public Position asRelativePosition() {
		return new Position(x.toInt(), y.toInt(), z.toInt());
	}
	*/
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
