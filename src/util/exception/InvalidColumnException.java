package util.exception;



public class InvalidColumnException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	public InvalidColumnException(int x, int y) {
		super("Nonexistent column, x: " + x + ", y: " + y);
	}
	
}
