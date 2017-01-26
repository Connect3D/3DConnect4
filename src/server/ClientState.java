package server;

public enum ClientState {
	
	PENDING,
	UNREADY,
	READY,
	INGAME;
	
	
	public String toString() {
		switch (this) {
			case PENDING: return "DISCONNECTED";
			case UNREADY: return "UNREADY";
			case READY:	  return "READY";
			case INGAME:  return "INGAME";
		}
		return null;
	}
	
}
