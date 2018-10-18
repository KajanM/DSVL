package com.dsvl.flood.service;

import com.dsvl.flood.File;
import com.dsvl.flood.Neighbour;

import java.net.InetAddress;
import java.util.List;

public interface SearchService {

    List<File> search(String fileName, List<Neighbour> neighbours, InetAddress nodeAddress, Integer nodeTcpPort);
}
