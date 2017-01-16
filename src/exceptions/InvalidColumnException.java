package exceptions;

public class InvalidColumnException extends RuntimeException {

	public InvalidColumnException(int x, int y) {
		super("Nonexistent column, x: " + x + ", y: " + y);
	}
	
}
