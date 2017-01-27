package util;

public interface Amount {

	public boolean satisfies(int i);
	
	
	
	public class Atleast implements Amount {
		
		private final int amount;
		
		public Atleast(int i) {
			amount = i;
		}
		
		public boolean satisfies(int i) {
			return i >= amount;
		}
	}
	
	
	
	public class Atmost implements Amount {
		
		private final int amount;
		
		public Atmost(int i) {
			amount = i;
		}
		
		public boolean satisfies(int i) {
			return i <= amount;
		}
	}
	
	
	
	public class Exact implements Amount {

		private final int amount;
		
		public Exact(int i) {
			amount = i;
		}
		
		public boolean satisfies(int i) {
			return i == amount;
		}
	}
	
	
}
