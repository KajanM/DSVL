package com.dsvl.flood.service.impl;

import com.dsvl.flood.*;
import com.dsvl.flood.model.ForwardedQuery;
import com.dsvl.flood.service.SearchService;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Override
    public void search(MessageObject msgObject, List<Neighbour> neighbours, InetAddress nodeAddress, Integer nodeUdpPort) {
        String searchMsg = UdpMsgBuilder.buildSearchMsg(msgObject.getSearch_ip(), msgObject.getSearch_udp_Port(), msgObject.getFile_name(), msgObject.getHops());
        ForwardedQuery forwardedQuery;
        boolean shouldForward;
        for (int i = 0; i < neighbours.size(); i++) {
            shouldForward = !(neighbours.get(i).getIpAddress().getHostAddress().equals(msgObject.getSenderIP()))
                    && !(neighbours.get(i).getIpAddress().getHostAddress().equals(msgObject.getSearch_ip()));
            if (shouldForward) {
                forwardedQuery = new ForwardedQuery(msgObject.getSearch_ip(),
                        msgObject.getSearch_udp_Port(),
                        msgObject.getFile_name(),
                        neighbours.get(i).getIpAddress().getHostAddress(),
                        neighbours.get(i).getUdpPort());

                if (Node.forwardedQueries.contains(forwardedQuery)) {
                    continue;
                }

                UdpHelper.sendMessage(searchMsg, neighbours.get(i).getIpAddress(), neighbours.get(i).getUdpPort());

                Node.forwardedQueries.add(forwardedQuery);
            }
        }
    }
}
