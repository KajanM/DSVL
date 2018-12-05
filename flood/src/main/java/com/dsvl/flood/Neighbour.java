package com.dsvl.flood;

import java.net.InetAddress;

public class Neighbour {
	private final InetAddress ipAddress;
	private final int udpPort;
    private int tTL;

	public Neighbour(InetAddress ipAddress, int udpPort){
		this.ipAddress = ipAddress;
		this.udpPort = udpPort;
	}	

	public InetAddress getIpAddress(){
		return this.ipAddress;
	}

	public int getUdpPort(){
		return this.udpPort;
	}

	public int gettTL() {
		return tTL;
	}

	public void settTL(int tTL) {
		this.tTL = tTL;
	}
}
