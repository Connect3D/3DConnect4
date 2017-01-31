package server;

import java.util.EnumMap;

import game.*;
import util.exception.protocol.*;



public class ServerSideGame {
	
	private final EnumMap<Mark, ClientHandler> players = new EnumMap<Mark, ClientHandler>(Mark.class);
	private Board board;
	private Mark onTurn = Mark.X;
	private boolean ended = false;
	
	
	public ServerSideGame(ClientHandler p1, ClientHandler p2) {
		players.put(Mark.X, p1);
		players.put(Mark.O, p2);
	}

	
	public void doMove(ClientHandler client, int x, int y) throws IllegalMoveException, CommandForbiddenException {
		if (players.get(onTurn) == client && !ended) {
			if (Column.isValid(x, y)) {
				Column col = new Column(x, y);
				if (!board.isColumnFull(col)) {
					board.doMove(new Move(col, onTurn));
					onTurn = onTurn.opposite();
					if (board.getEnding() != Game.Ending.NOT_ENDED) {
						ended = true;
					}
				}
			}
			else {
				throw new IllegalMoveException();
			}
		}
		else {
			throw new CommandForbiddenException();
		}
	}
	
	
	public Game.Ending getEnding() {
		return board.getEnding();
	}
	
}
