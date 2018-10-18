package com.dsvl.flood;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UdpMsgBuilderTest {

    @Test
    public void buildSearchMsg() {
        // length SER IP port file_name hops
        String serMsg = UdpMsgBuilder.buildSearchMsg("127.0.0.1", 45555, "Lord of the rings", 0);
        assertEquals("0044 SER 127.0.0.1 45555 Lord of the rings 0", serMsg);
    }

    @Test
    public void buildRegisterMsg() {
        //length REG IP_address port_no username
        String regMsg = UdpMsgBuilder.buildRegisterMsg("127.0.0.1", 45555, "dsvl");
        assertEquals("0029 REG 127.0.0.1 45555 dsvl", regMsg);
    }
}