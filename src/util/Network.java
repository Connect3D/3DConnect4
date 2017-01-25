package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Network {

	
	public static int createPort(String port) {
		int portInt = 0;
		try {
			portInt = Integer.parseInt(port);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: port " + port + " is not an integer.");
			System.exit(0);
		}
		return portInt;
	}

	
	public static InetAddress createAddress(String host) {
		InetAddress address = null;
		try {
			address = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			System.out.println("ERROR: Localhost " + host + " not found");
			System.exit(0);
		}
		return address;
	}
	
	
}
