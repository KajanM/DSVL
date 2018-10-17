package com.dsvl.flood;

import java.net.InetAddress;

public class Neighbour {
	private final InetAddress address;
	private final int port;
	private final String username;

	public Neighbour(InetAddress address, int port, String username){
		this.address = address;
		this.port = port;
		this.username = username;
	}	

	public InetAddress getAddress(){
		return this.address;
	}

	public String getUsername(){
		return this.username;
	}

	public int getPort(){
		return this.port;
	}
}
