package com.dsvl.flood;

import java.net.InetAddress;

public class Neighbour {
	private final InetAddress ipAddress;
	private final int udpPort;

	public Neighbour(InetAddress ipAddress, int udpPort){
		this.ipAddress = ipAddress;
		this.udpPort = udpPort;
	}	

	public InetAddress getAddress(){
		return this.ipAddress;
	}

	public int getPort(){
		return this.udpPort;
	}
}
