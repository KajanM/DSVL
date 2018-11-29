package com.dsvl.flood.service;

import com.dsvl.flood.File;
import com.dsvl.flood.MessageObject;
import com.dsvl.flood.Neighbour;

import java.net.InetAddress;
import java.util.List;

public interface SearchService {

    List<File> search(MessageObject msgObject, List<Neighbour> neighbours, InetAddress nodeAddress, Integer nodeTcpPort);
}
