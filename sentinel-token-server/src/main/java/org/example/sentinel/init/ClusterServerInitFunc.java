package org.example.sentinel.init;

import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterParamFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.fasterxml.jackson.core.type.TypeReference;
import org.example.sentinel.constants.CommonConstants;

import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author fanls
 */
public class ClusterServerInitFunc implements InitFunc {

    @Override
    public void init() throws Exception {
        // 构建连接Nacos信息
        Properties properties = buildProperties();

        registerClusterRuleSupplier(properties);

        initTokenServerNamespaces(properties);

        initServerTransportConfig(properties);
    }

    private void registerClusterRuleSupplier(Properties properties) {
        // Register cluster flow rule property supplier which creates data source by namespace.
        ClusterFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<FlowRule>> ds = new NacosDataSource<>(properties, CommonConstants.GROUP_ID,
                    namespace + CommonConstants.FLOW_DATA_ID_POSTFIX,
                    source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                    }));
            return ds.getProperty();
        });
        // Register cluster parameter flow rule property supplier.
        ClusterParamFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<ParamFlowRule>> ds = new NacosDataSource<>(properties, CommonConstants.GROUP_ID,
                    namespace + CommonConstants.PARAM_FLOW_DATA_ID_POSTFIX,
                    source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
                    }));
            return ds.getProperty();
        });
    }

    private void initTokenServerNamespaces(Properties properties) {
        // Server namespace set (scope) data source.
        ReadableDataSource<String, Set<String>> namespaceDs = new NacosDataSource<>(properties, CommonConstants.GROUP_ID,
                CommonConstants.SERVER_NAMESPACE_SET_DATA_ID, source -> JSON.parseObject(source, new TypeReference<Set<String>>() {
        }));
        ClusterServerConfigManager.registerNamespaceSetProperty(namespaceDs.getProperty());
    }

    private void initServerTransportConfig(Properties properties) {
        // Server transport configuration data source.
        ReadableDataSource<String, ServerTransportConfig> transportConfigDs = new NacosDataSource<>(properties,
                CommonConstants.GROUP_ID, CommonConstants.SERVER_TRANSPORT_CONFIG_DATA_ID,
                source -> JSON.parseObject(source, new TypeReference<ServerTransportConfig>() {
                }));
        ClusterServerConfigManager.registerServerTransportProperty(transportConfigDs.getProperty());
    }

    private Properties buildProperties() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, "124.222.130.129:8848");
        properties.put(PropertyKeyConst.NAMESPACE, "563828cf-ce66-4318-8431-a619741ace27");
        return properties;
    }

}
