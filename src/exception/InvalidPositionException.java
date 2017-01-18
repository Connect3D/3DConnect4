package exception;

public class InvalidPositionException extends RuntimeException {

	public InvalidPositionException(int x, int y, int z) {
		super("Nonexistent position, x: " + x + ", y: " + y + ", z: " + z);
	}
	
}
