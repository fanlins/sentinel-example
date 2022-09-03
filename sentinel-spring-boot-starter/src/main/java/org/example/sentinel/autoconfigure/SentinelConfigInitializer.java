package org.example.sentinel.autoconfigure;


import lombok.extern.slf4j.Slf4j;
import org.example.sentinel.constants.CommonConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

/**
 * @author fanls
 */
@Slf4j
@ConditionalOnProperty(
        prefix = "sentinel",
        name = {"enabled"},
        matchIfMissing = true
)
public class SentinelConfigInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        log.info(" [SENTINEL EX] Initialize sentinel configure... ");
        ConfigurableEnvironment environment = context.getEnvironment();
        PropertiesPropertySource propertySource = new PropertiesPropertySource("sentinel-autoconfigure", this.properties());
        environment.getPropertySources().addLast(propertySource);
        log.info(" [SENTINEL EX] sentinel configured!");
    }

    public Properties properties() {
        Properties properties = new Properties();
        // 连接控制台
        properties.setProperty("spring.cloud.sentinel.transport.dashboard", "${sentinel.dashboard}");
        // 资源限流规则
        properties.setProperty("spring.cloud.sentinel.datasource.flow.nacos.server-addr", "${sentinel.nacos.server-addr}");
        properties.setProperty("spring.cloud.sentinel.datasource.flow.nacos.namespace", "${sentinel.nacos.namespace}");
        properties.setProperty("spring.cloud.sentinel.datasource.flow.nacos.dataId", "${spring.application.name}" + CommonConstants.FLOW_DATA_ID_POSTFIX);
        properties.setProperty("spring.cloud.sentinel.datasource.flow.nacos.groupId", CommonConstants.GROUP_ID);
        properties.setProperty("spring.cloud.sentinel.datasource.flow.nacos.data-type", CommonConstants.NACOS_DATA_TYPE);
        properties.setProperty("spring.cloud.sentinel.datasource.flow.nacos.rule-type", CommonConstants.FLOW_NACOS_RULE_TYPE);
        // 热点参数限流规则
        properties.setProperty("spring.cloud.sentinel.datasource.param-flow.nacos.server-addr", "${sentinel.nacos.server-addr}");
        properties.setProperty("spring.cloud.sentinel.datasource.param-flow.nacos.namespace", "${sentinel.nacos.namespace}");
        properties.setProperty("spring.cloud.sentinel.datasource.param-flow.nacos.dataId", "${spring.application.name}" + CommonConstants.PARAM_FLOW_DATA_ID_POSTFIX);
        properties.setProperty("spring.cloud.sentinel.datasource.param-flow.nacos.groupId", CommonConstants.GROUP_ID);
        properties.setProperty("spring.cloud.sentinel.datasource.param-flow.nacos.data-type", CommonConstants.NACOS_DATA_TYPE);
        properties.setProperty("spring.cloud.sentinel.datasource.param-flow.nacos.rule-type", CommonConstants.PARAM_FLOW_NACOS_RULE_TYPE);
        // 熔断降级规则
        properties.setProperty("spring.cloud.sentinel.datasource.degrade.nacos.server-addr", "${sentinel.nacos.server-addr}");
        properties.setProperty("spring.cloud.sentinel.datasource.degrade.nacos.namespace", "${sentinel.nacos.namespace}");
        properties.setProperty("spring.cloud.sentinel.datasource.degrade.nacos.dataId", "${spring.application.name}" + CommonConstants.DEGRADE_DATA_ID_POSTFIX);
        properties.setProperty("spring.cloud.sentinel.datasource.degrade.nacos.groupId", CommonConstants.GROUP_ID);
        properties.setProperty("spring.cloud.sentinel.datasource.degrade.nacos.data-type", CommonConstants.NACOS_DATA_TYPE);
        properties.setProperty("spring.cloud.sentinel.datasource.degrade.nacos.rule-type", CommonConstants.DEGRADE_NACOS_RULE_TYPE);
        // 系统保护规则
        properties.setProperty("spring.cloud.sentinel.datasource.system.nacos.server-addr", "${sentinel.nacos.server-addr}");
        properties.setProperty("spring.cloud.sentinel.datasource.system.nacos.namespace", "${sentinel.nacos.namespace}");
        properties.setProperty("spring.cloud.sentinel.datasource.system.nacos.dataId", "${spring.application.name}" + CommonConstants.SYSTEM_DATA_ID_POSTFIX);
        properties.setProperty("spring.cloud.sentinel.datasource.system.nacos.groupId", CommonConstants.GROUP_ID);
        properties.setProperty("spring.cloud.sentinel.datasource.system.nacos.data-type", CommonConstants.NACOS_DATA_TYPE);
        properties.setProperty("spring.cloud.sentinel.datasource.system.nacos.rule-type", CommonConstants.SYSTEM_NACOS_RULE_TYPE);
        // 授权规则
        properties.setProperty("spring.cloud.sentinel.datasource.authority.nacos.server-addr", "${sentinel.nacos.server-addr}");
        properties.setProperty("spring.cloud.sentinel.datasource.authority.nacos.namespace", "${sentinel.nacos.namespace}");
        properties.setProperty("spring.cloud.sentinel.datasource.authority.nacos.dataId", "${spring.application.name}" + CommonConstants.AUTHORITY_DATA_ID_POSTFIX);
        properties.setProperty("spring.cloud.sentinel.datasource.authority.nacos.groupId", CommonConstants.GROUP_ID);
        properties.setProperty("spring.cloud.sentinel.datasource.authority.nacos.data-type", CommonConstants.NACOS_DATA_TYPE);
        properties.setProperty("spring.cloud.sentinel.datasource.authority.nacos.rule-type", CommonConstants.AUTHORITY_NACOS_RULE_TYPE);
        log.info(" [SENTINEL EX] Configure sentinel with properties {}", properties);
        return properties;
    }


}
