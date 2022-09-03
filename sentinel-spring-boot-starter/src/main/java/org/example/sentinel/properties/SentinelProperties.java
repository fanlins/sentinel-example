package org.example.sentinel.properties;

import lombok.Data;

/**
 * @author fanls
 */
@Data
public class SentinelProperties {

    private String dashboard;

    private Nacos nacos;

    @Data
    public static class Nacos {

        private String serverAddr;

        private String namespace;
    }
}
