package com.huawei.android.hms.agent.common;

/**
 * API 瀹炵幇绫诲熀绫伙紝鐢ㄤ簬澶勭悊鍏叡鎿嶄綔
 * 鐩墠瀹炵幇鐨勬槸client鐨勮繛鎺ュ強鍥炶皟
 */
public abstract class BaseApiAgent implements IClientConnectCallback {
    protected void connect() {
        HMSAgentLog.d("connect");
        ApiClientMgr.INST.connect(this, true);
    }
}
