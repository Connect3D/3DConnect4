package util.exception;



public class ColumnFullException extends RuntimeException {

	
	public ColumnFullException(int x, int y) {
		super("Column was full, x: " + x + ", y: " + y);
	}
	
}
