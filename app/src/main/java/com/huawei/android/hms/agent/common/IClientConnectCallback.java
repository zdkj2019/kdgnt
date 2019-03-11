package com.huawei.android.hms.agent.common;

import com.huawei.hms.api.HuaweiApiClient;

/**
 * HuaweiApiClient 杩炴帴缁撴灉鍥炶皟
 */
public interface IClientConnectCallback {
    /**
     * HuaweiApiClient 杩炴帴缁撴灉鍥炶皟
     * @param rst 缁撴灉鐮�
     * @param client HuaweiApiClient 瀹炰緥
     */
    void onConnect(int rst, HuaweiApiClient client);
}
