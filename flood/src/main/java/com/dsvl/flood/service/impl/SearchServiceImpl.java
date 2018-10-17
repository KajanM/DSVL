package com.dsvl.flood.service.impl;

import com.dsvl.flood.File;
import com.dsvl.flood.Neighbour;
import com.dsvl.flood.UdpHelper;
import com.dsvl.flood.service.SearchService;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.List;
import java.util.StringJoiner;

import static com.dsvl.flood.Constants.Protocol.SER;

@Service
public class SearchServiceImpl implements SearchService {

    @Override
    public  List<File> search(String fileName, List<Neighbour> neighbours, InetAddress nodeAddress, Integer nodePort) {
        // length SER IP port file_name hops

        int length = 4 + 1 + 3 + 1 + nodeAddress.getHostAddress().length() + 1 + nodePort.toString().length() + 1 + fileName.length() + 1 + 1;
        // TODO: kajan, this logic can be separated out for reusability
        StringJoiner searchMessage = new StringJoiner(" ");
        searchMessage.add(String.format("%04d", length))
                .add(SER.name())
                .add(nodeAddress.getHostAddress())
                .add(nodePort.toString())
                .add(fileName)
                .add("0");
        neighbours.stream()
                .forEach((neighbour) -> UdpHelper.sendMessage(searchMessage.toString(), neighbour.getAddress(), neighbour.getPort()));
        return null;
    }
}
