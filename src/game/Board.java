package game;

import java.util.ArrayList;
import java.util.Arrays;

import util.exception.ColumnFullException;


public class Board {
	
	public static final int WIDTH = 4;
	public static final int DEPTH = 4;
	public static final int HEIGHT = 4;
	public static final int N_FIELDS = WIDTH * DEPTH * HEIGHT;
	public static final String DELIMETER = "+---+---+---+---+";
	
	
	public Mark[] fields;
	private Game.Ending ending;
	
	
	// default constructor
	public Board() {
		fields = new Mark[N_FIELDS];
		Arrays.fill(fields, Mark.EMPTY);
		ending = Game.Ending.NOT_ENDED;
	}
	
	
	// copy constructor (more efficient than deepCopy function)
	public Board(Board board) {
		fields = new Mark[N_FIELDS];
		for (int i = 0; i < N_FIELDS; ++i) {
			fields[i] = board.fields[i];
		}
		ending = board.ending.copy();
	}
	
	
	// pass by value to avoid messing with board from outside of class
	public Game.Ending getEnding() {
		return ending.copy();
	}
	
	
	public boolean isColumnFull(Column column) {
		return fields[Position.indexOf(column.x, column.y, HEIGHT - 1)] != Mark.EMPTY;
	}
	
	public void doMove(Move move) {
		Position position = cascade(move.column);
		fields[position.index()] = move.mark;
		ending = checkEndingFor(position, move.mark);
	}
	
	
	public ArrayList<Column> possibleColumns() {
		ArrayList<Column> possible = new ArrayList<Column>();
		for (int x = 0; x < WIDTH; ++x) {
			for (int y = 0; y < DEPTH; ++y) {
				Column column = new Column(x, y);
				if (!isColumnFull(column)) {
					possible.add(column);
				}
			}
		}
		return possible;
	}
	
	
	public String getRowAsString(int y, int z) {
		String row = "|";
		Direction direction = new Direction(Direction.Axis.POSITIVE, Direction.Axis.NEUTRAL, Direction.Axis.NEUTRAL);
		for (Position p = new Position(0, y, z); p != null; p = p.inDirection(direction)) {
			row += " " + fields[p.index()].toString() + " |";
		}
		return row;
	}
	
	
	//==============================================//
	//             PRIVATE FUNCTIONS                //
	//==============================================//
	
	
	private Game.Ending checkEndingFor(Position position, Mark mark) {
		if (isFull()) return Game.Ending.DRAW;
		for (Direction d = Direction.begin(); !d.equals(Direction.center()); d = d.next()) {
			int consecutive = 1 + consecutiveMarks(position, mark, d) + consecutiveMarks(position, mark, d.opposite());
			if (consecutive >= Game.CONSECUTIVE_MARKS_TO_WIN) {
				if (mark == Mark.X) return Game.Ending.X_WINS;
				if (mark == Mark.O) return Game.Ending.O_WINS;
			}
		}
		return Game.Ending.NOT_ENDED;
	}
	
	
	// does not need to be public, because getEnding() also tells if the board is full (DRAW) and is much faster
	private boolean isFull() {
		for (int x = 0; x < WIDTH; ++x) {
			for (int y = 0; y < DEPTH; ++y) {
				if (!isColumnFull(new Column(x, y))) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	private Position cascade(Column column) {
		for (int z = 0; z < HEIGHT; ++z) {
			if (fields[Position.indexOf(column.x, column.y, z)] == Mark.EMPTY) {
				return new Position(column.x, column.y, z);
			}
		}
		throw new ColumnFullException(column.x, column.y);
	}
	
	
	private int consecutiveMarks(Position position, Mark mark, Direction direction) {
		Position next = position.inDirection(direction);
		int amount = 0;
		while (next != null && fields[next.index()] == mark) {
			next = next.inDirection(direction);
			++amount;
		}
		return amount;
	}
	
}