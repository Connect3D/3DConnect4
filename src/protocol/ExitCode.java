package protocol;

public enum ExitCode {

	WON,
	LOST,
	DRAW,
	TIMEOUT,
	FORFEITURE;
	
	public String toString() {
		switch (this) {
			case WON: return "WON";
			case LOST: return "LOST";
			case DRAW: return "DRAW";
			case TIMEOUT: return "TIMEOUT";
			case FORFEITURE: return "FORFEITURE";
		}
		return "";
	}
	
}
