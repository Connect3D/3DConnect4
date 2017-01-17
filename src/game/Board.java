package game;

import java.util.Arrays;

import exceptions.ColumnFullException;


public class Board {
	
	public static final int WIDTH = 4;
	public static final int DEPTH = 4;
	public static final int HEIGHT = 4;
	public static final int N_FIELDS = WIDTH * DEPTH * HEIGHT;
	
	
	private Mark[] fields;
	private Position lastMove;
	
	// default constructor
	public Board() {
		fields = new Mark[N_FIELDS];
		Arrays.fill(fields, Mark.EMPTY);
		lastMove = null;
	}
	
	
	// copy constructor (more efficient than deepCopy function)
	public Board(Board board) {
		fields = new Mark[N_FIELDS];
		for (int i = 0; i < N_FIELDS; ++i) {
			fields[i] = board.fields[i];
		}
		lastMove = board.lastMove;
	}
	
	
	public boolean isColumnFull(Column column) {
		return fields[Position.indexOf(column.x, column.y, HEIGHT - 1)] != Mark.EMPTY;
	}
	
	
	public boolean isFull() {
		for (int x = 0; x < WIDTH; ++x) {
			for (int y = 0; y < DEPTH; ++y) {
				if (!isColumnFull(new Column(x, y))) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	public void doMove(Column column, Mark mark) {
		fields[cascade(column).index()] = mark;
	}
	
	
	private Position cascade(Column column) {
		for (int z = 0; z < HEIGHT; ++z) {
			if (fields[Position.indexOf(column.x, column.y, z)] == Mark.EMPTY) {
				return new Position(column.x, column.y, z);
			}
		}
		throw new ColumnFullException(column.x, column.y);
	}
	
	
	private int extendsInDirection(Position position, Direction direction) {
		Mark mark = fields[position.index()];
		Position next = position.inDirection(direction);
		int amount = 0;
		while (next != null && fields[next.index()] == mark) {
			next = next.inDirection(direction);
			++amount;
		}
		return amount;
	}

}