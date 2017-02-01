package util.exception;



public class InvalidPositionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	
	public InvalidPositionException(int x, int y, int z) {
		super("Nonexistent position, x: " + x + ", y: " + y + ", z: " + z);
	}
	
}
