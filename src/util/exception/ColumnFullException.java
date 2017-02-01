package util.exception;



public class ColumnFullException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	
	public ColumnFullException(int x, int y) {
		super("Column was full, x: " + x + ", y: " + y);
	}
	
}
