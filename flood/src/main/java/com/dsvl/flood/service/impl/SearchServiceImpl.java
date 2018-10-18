package com.dsvl.flood.service.impl;

import com.dsvl.flood.File;
import com.dsvl.flood.Neighbour;
import com.dsvl.flood.UdpHelper;
import com.dsvl.flood.UdpMsgBuilder;
import com.dsvl.flood.service.SearchService;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Override
    public List<File> search(String fileName, List<Neighbour> neighbours, InetAddress nodeAddress, Integer nodeTcpPort) {
        String searchMsg = UdpMsgBuilder.buildSearchMsg(nodeAddress.getHostAddress(), nodeTcpPort, fileName, 0);
        neighbours.stream()
                .forEach((neighbour) -> UdpHelper.sendMessage(searchMsg, neighbour.getAddress(), neighbour.getPort()));
        // TODO: kajan, UDP seraching should be fire and forget, so do not return anything here
        return null;
    }
}
