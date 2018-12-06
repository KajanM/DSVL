package com.dsvl.flood.service;

import com.dsvl.flood.MessageObject;
import com.dsvl.flood.Neighbour;

import java.net.InetAddress;
import java.util.List;

public interface SearchService {

    void search(MessageObject msgObject, List<Neighbour> neighbours, InetAddress nodeAddress, Integer nodeTcpPort);
}
