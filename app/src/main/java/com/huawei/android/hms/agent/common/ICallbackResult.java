package com.huawei.android.hms.agent.common;

/**
 * 鍥炶皟鎺ュ彛
 */
public interface ICallbackResult<R> {
    /**
     * 鍥炶皟鎺ュ彛
     * @param rst 缁撴灉鐮�
     * @param result 缁撴灉淇℃伅
     */
    void onResult(int rst, R result);
}
