package game;


// no access specifier for package scope
// TODO: remove public scope again
public class Direction {
	
	
	public static Direction begin() {
		return new Direction(Axis.NEGATIVE, Axis.NEGATIVE, Axis.NEGATIVE);
	}
	
	
	public static Direction center() {
		return new Direction(Axis.NEUTRAL, Axis.NEUTRAL, Axis.NEUTRAL);
	}
	
	
	public static Direction end() {
		return new Direction(Axis.POSITIVE, Axis.POSITIVE, Axis.POSITIVE);
	}
	
	
	public final Axis x_axis;
	public final Axis y_axis;
	public final Axis z_axis;
	
	
	public Direction opposite() {
		return new Direction(this.x_axis.opposite(), this.y_axis.opposite(), this.z_axis.opposite());
	}
	
	
	public Direction next() {
		if (z_axis != Axis.POSITIVE) return new Direction(x_axis, y_axis, z_axis.increment());
		if (y_axis != Axis.POSITIVE) return new Direction(x_axis, y_axis.increment(), z_axis.increment());
		if (x_axis != Axis.POSITIVE) return new Direction(x_axis.increment(), y_axis.increment(), z_axis.increment());
		else return null;
	}
	
	
	public boolean equals(Direction other) {
		return x_axis == other.x_axis && y_axis == other.y_axis && z_axis == other.z_axis;
	}
	
	
	// constructor is never needed outside of class
	//TODO: make private again
	public Direction(Axis x, Axis y, Axis z) {
		x_axis = x;
		y_axis = y;
		z_axis = z;
	}
	
	
	// now also has package scope, because Direction has package scope
	public enum Axis {
		
		NEGATIVE, NEUTRAL, POSITIVE;
		
		public Axis opposite() {
			if (this == NEGATIVE) return POSITIVE;
			if (this == POSITIVE) return NEGATIVE;
			return NEUTRAL;
		}
		
		public Axis increment() {
			if (this == NEGATIVE) return NEUTRAL;
			if (this == NEUTRAL) return POSITIVE;
			return NEGATIVE;
		}
		
		public int toInt() {
			if (this == NEGATIVE) return -1;
			if (this == POSITIVE) return 1;
			return 0;
		}
		
	}
	
}


