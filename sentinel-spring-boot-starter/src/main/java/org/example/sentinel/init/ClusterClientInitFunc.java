package org.example.sentinel.init;

import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;
import org.example.sentinel.constants.CommonConstants;
import org.example.sentinel.entity.ClusterGroupEntity;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * @author fanls
 */
public class ClusterClientInitFunc implements InitFunc {

    @Override
    public void init() throws Exception {
        // 构建连接Nacos信息
        Properties properties = buildProperties();
        // 初始化token-server信息
        initClientServerAssignProperty(properties);
    }

    private void initClientServerAssignProperty(Properties properties) {
        // Cluster map format:
        // [{"clientSet":["112.12.88.66@8729","112.12.88.67@8727"],"ip":"112.12.88.68","machineId":"112.12.88.68@8728","port":11111}]
        // machineId: <ip@commandPort>, commandPort for port exposed to Sentinel dashboard (transport module)
        ReadableDataSource<String, ClusterClientAssignConfig> clientAssignDs = new NacosDataSource<>(properties, CommonConstants.GROUP_ID,
                CommonConstants.CLUSTER_MAP_DATA_ID, source -> {
            List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<List<ClusterGroupEntity>>() {
            });
            return Optional.ofNullable(groupList)
                    .flatMap(this::extractClientAssignment)
                    .orElse(null);
        });
        ClusterClientConfigManager.registerServerAssignProperty(clientAssignDs.getProperty());
    }

    private Optional<ClusterClientAssignConfig> extractClientAssignment(List<ClusterGroupEntity> groupList) {
        if (groupList.stream().anyMatch(this::machineEqual)) {
            return Optional.empty();
        }
        // Build client assign config from the client set of target server group.
        for (ClusterGroupEntity group : groupList) {
            if (group.getClientSet().contains(getCurrentMachineId())) {
                String ip = group.getIp();
                Integer port = group.getPort();
                return Optional.of(new ClusterClientAssignConfig(ip, port));
            }
        }
        return Optional.empty();
    }

    private boolean machineEqual(/*@Valid*/ ClusterGroupEntity group) {
        return getCurrentMachineId().equals(group.getMachineId());
    }

    private String getCurrentMachineId() {
        // Note: this may not work well for container-based env.
        return HostNameUtil.getIp() + CommonConstants.SEPARATOR + TransportConfig.getRuntimePort();
    }

    private Properties buildProperties() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, "124.222.130.129:8848");
        properties.put(PropertyKeyConst.NAMESPACE, "563828cf-ce66-4318-8431-a619741ace27");
        return properties;
    }

}
