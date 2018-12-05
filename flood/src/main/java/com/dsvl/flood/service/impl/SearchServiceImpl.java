package com.dsvl.flood.service.impl;

import com.dsvl.flood.*;
import com.dsvl.flood.service.SearchService;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Override
//    length SER IP port file_name hops
    public List<File> search(MessageObject msgObject, List<Neighbour> neighbours, InetAddress nodeAddress, Integer nodeUdpPort) {
        String searchMsg = UdpMsgBuilder.buildSearchMsg(msgObject.getSearch_ip(), msgObject.getSearch_udp_Port(), msgObject.getFile_name(), msgObject.getHops());
        for (int i = 0; i < neighbours.size(); i++) {
            if (!(neighbours.get(i).getAddress().toString().equals(msgObject.getSenderIP())) && !(neighbours.get(i).getUdpPort()==msgObject.getSenderPort())){
                UdpHelper.sendMessage(searchMsg, neighbours.get(i).getAddress(), neighbours.get(i).getUdpPort());
            }
        }
    return null;}
}
