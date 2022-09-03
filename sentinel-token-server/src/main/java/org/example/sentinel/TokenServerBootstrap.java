package org.example.sentinel;

import com.alibaba.csp.sentinel.cluster.server.ClusterTokenServer;
import com.alibaba.csp.sentinel.cluster.server.SentinelDefaultTokenServer;

/**
 * @author fanls
 */
public class TokenServerBootstrap {

    public static void main(String[] args) throws Exception {
        // Not embedded mode by default (alone mode).
        ClusterTokenServer tokenServer = new SentinelDefaultTokenServer();
        // Start the server.
        tokenServer.start();
    }
}
