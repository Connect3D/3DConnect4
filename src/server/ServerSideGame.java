package server;

import com.google.common.collect.EnumHashBiMap;

import game.*;
import protocol.command.Exit;
import util.exception.CommandForbiddenException;
import util.exception.IllegalMoveException;



public class ServerSideGame {
	
	private final EnumHashBiMap<Mark, ClientHandler> players = EnumHashBiMap.create(Mark.class);		//new EnumMap<Mark, ClientHandler>(Mark.class);
	private Board board;
	private Mark onTurn = Mark.X;
	private Move lastMove = null;
	private boolean sync = true;
	private boolean hasEnded = false;
	
	
	public ServerSideGame(ClientHandler p1, ClientHandler p2) {
		board = new Board();
		players.put(Mark.X, p1);
		players.put(Mark.O, p2);
	}

	
	public void doMove(ClientHandler client, int x, int y) throws IllegalMoveException, CommandForbiddenException {
		if (players.get(onTurn) == client && !hasEnded) {
			if (Column.isValid(x, y)) {
				Column col = new Column(x, y);
				if (!board.isColumnFull(col)) {
					lastMove = new Move(col, onTurn);
					board.doMove(lastMove);
					onTurn = onTurn.opposite();
					sync = false;
					if (board.getEnding() != Game.Ending.NOT_ENDED) {
						hasEnded = true;
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
	
	
	public Exit getEndingFor(ClientHandler client) {
		if (board.getEnding() == Game.Ending.NOT_ENDED) return null;
		if (board.getEnding() == Game.Ending.DRAW) return Exit.DRAW;
		if (board.getEnding() == Game.Ending.X_WINS && players.inverse().get(client) == Mark.X) return Exit.WON;
		return Exit.LOST;
	}
	
	
	public ClientHandler getPlayer(Mark m) {
		return players.get(m);
	}
	
	
	public Move getLastMove() {
		return lastMove;
	}

	
	public boolean isSync() {
		return sync;
	}
	
	
	public void setSync() {
		sync = true;
	}
	
}
