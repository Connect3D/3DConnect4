package game;

import java.util.Arrays;


public class Board {
	
	public static final int WIDTH = 4;
	public static final int DEPTH = 4;
	public static final int HEIGHT = 4;
	public static final int N_FIELDS = WIDTH * DEPTH * HEIGHT;
	
	
	private Mark[] fields;
	
	
	// default constructor
	public Board() {
		fields = new Mark[N_FIELDS];
		Arrays.fill(fields, Mark.EMPTY);
	}
	
	
	// copy constructor (more efficient than deepCopy function)
	public Board(Board board) {
		for (int i = 0; i < N_FIELDS; ++i) {
			fields[i] = board.fields[i];
		}
	}
	
	
	// should throw column full exception
	public void doMove(Column column, Mark mark) {
		
	}
	
	
	private static Position cascade(Column column) {
		return null;
	}
	
	
	// internal class for dealing with positions in 3D
	private static class Position {
		
		public final int x, y, z;
		
		// somehow it is not necessary to declare function might throw illegalArgument???
		public Position(int _x, int _y, int _z) {
			if (_x < 0 || _x >= WIDTH || _y < 0 || _y >= DEPTH || _z < 0 || _z >= HEIGHT) {
				throw new IllegalArgumentException();
			}
			x = _x; 
			y = _y; 
			z = _z;
		}
		
		public int index() {
			return 0;
		}
		
	}

}