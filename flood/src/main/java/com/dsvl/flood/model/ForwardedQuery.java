package com.dsvl.flood.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Used to keep track of sent queries
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForwardedQuery {
    private String msgOwnerIp;
    private int msgOwnerUdpPort;
    private String searchTerm;
    private String neighbourIp;
    private int neighbourPort;
}
