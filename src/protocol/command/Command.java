package protocol.command;

import util.exception.*;



public interface Command {
	
	
	public static Type getType(String[] array, Direction direction) throws CommandUnsupportedException {
		if (array != null && array.length > 0) {
			
			if (array[0].equals("ERROR")) return Type.ERROR;
			if (array[0].equals("EXIT")) return Type.EXIT;
			
			if (Action.NAME.containsValue(array[0]) && Acknowledgement.NAME.containsValue(array[0])) {
				if (direction == Direction.CLIENT_TO_SERVER) return Type.ACTION;
				if (direction == Direction.SERVER_TO_CLIENT) return Type.ACKNOWLEDGEMENT;
			}
			else if (Action.NAME.containsValue(array[0])) {
				return Type.ACTION;
			}
			else if (Acknowledgement.NAME.containsValue(array[0])) {
				return Type.ACKNOWLEDGEMENT;
			}
		}
		throw new CommandUnsupportedException();
	}

	
	public enum Direction {
		CLIENT_TO_SERVER,
		SERVER_TO_CLIENT,
		BIDIRECTIONAL;
	}
	
	public enum Type {
		ACTION,
		ACKNOWLEDGEMENT,
		EXIT,
		ERROR;
	}
	
}