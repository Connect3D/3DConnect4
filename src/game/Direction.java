package game;



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
	
	
	public final Axis xaxis;
	public final Axis yaxis;
	public final Axis zaxis;
	
	
	public Direction(Axis x, Axis y, Axis z) {
		xaxis = x;
		yaxis = y;
		zaxis = z;
	}
	
	
	public Direction opposite() {
		return new Direction(this.xaxis.opposite(), 
				this.yaxis.opposite(), this.zaxis.opposite());
	}
	
	
	public Direction next() {
		if (zaxis != Axis.POSITIVE) {
			return new Direction(xaxis, yaxis, zaxis.increment());
		}
		if (yaxis != Axis.POSITIVE) {
			return new Direction(xaxis, yaxis.increment(), zaxis.increment());
		}
		if (xaxis != Axis.POSITIVE) {
			return new Direction(xaxis.increment(), yaxis.increment(), zaxis.increment());
		} else {
			return null;
		}
	}
	
	
	public boolean equals(Direction other) {
		return xaxis == other.xaxis && yaxis == other.yaxis && zaxis == other.zaxis;
	}
	
	
	// now also has package scope, because Direction has package scope
	public enum Axis {
		
		NEGATIVE, NEUTRAL, POSITIVE;
		
		public Axis opposite() {
			if (this == NEGATIVE) {
				return POSITIVE;
			}
			if (this == POSITIVE) {
				return NEGATIVE;
			}
			return NEUTRAL;
		}
		
		public Axis increment() {
			if (this == NEGATIVE) {
				return NEUTRAL;
			}
			if (this == NEUTRAL) {
				return POSITIVE;
			}
			return NEGATIVE;
		}
		
		public int toInt() {
			if (this == NEGATIVE) {
				return -1;
			}
			if (this == POSITIVE) { 
				return 1;
			}
			return 0;
		}
		
	}
	
}


