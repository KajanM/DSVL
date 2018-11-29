package com.dsvl.flood.model;

import com.dsvl.flood.File;
import com.dsvl.flood.Neighbour;

import java.util.List;

/**
 * All data that need to be sent to front-end
 */
public class Data {
    NodeDetails nodeDetails;
    List<Log> logs;
    List<Neighbour> neighbours;
    List<File> files;

    public Data() {
    }

    public void setNodeDetails(NodeDetails nodeDetails) {
        this.nodeDetails = nodeDetails;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public void setNeighbours(List<Neighbour> neighbours) {
        this.neighbours = neighbours;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
