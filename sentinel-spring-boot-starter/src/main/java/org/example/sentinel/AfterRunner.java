package org.example.sentinel;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author fanls
 */
@Component
public class AfterRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 指定当前身份为 Token Client
        ClusterStateManager.applyState(ClusterStateManager.CLUSTER_CLIENT);
    }
}
