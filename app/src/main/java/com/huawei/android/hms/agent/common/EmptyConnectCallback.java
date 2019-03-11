package com.huawei.android.hms.agent.common;

import com.huawei.hms.api.HuaweiApiClient;

/**
 * 杩炴帴client绌哄洖璋�
 */
public class EmptyConnectCallback implements IClientConnectCallback {

    private String msgPre;

    public EmptyConnectCallback(String msgPre){
        this.msgPre = msgPre;
    }

    /**
     * HuaweiApiClient 杩炴帴缁撴灉鍥炶皟
     *
     * @param rst    缁撴灉鐮�
     * @param client HuaweiApiClient 瀹炰緥
     */
    @Override
    public void onConnect(int rst, HuaweiApiClient client) {
        HMSAgentLog.d(msgPre + rst);
    }
}