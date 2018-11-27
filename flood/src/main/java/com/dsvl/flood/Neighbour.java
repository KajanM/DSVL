package com.dsvl.flood;

import java.net.InetAddress;

public class Neighbour {
	private final InetAddress address;
	private final int port;

	public Neighbour(InetAddress address, int port){
		this.address = address;
		this.port = port;
	}	

	public InetAddress getAddress(){
		return this.address;
	}

	public int getPort(){
		return this.port;
	}
}
