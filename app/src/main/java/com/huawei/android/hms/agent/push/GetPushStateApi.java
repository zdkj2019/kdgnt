package com.huawei.android.hms.agent.push;

import android.os.Handler;
import android.os.Looper;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.CallbackCodeRunnable;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.StrUtils;
import com.huawei.android.hms.agent.common.ThreadUtil;
import com.huawei.android.hms.agent.push.handler.GetPushStateHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

/**
 * 鑾峰彇push鐘舵�佺殑鎺ュ彛銆�
 */
public class GetPushStateApi extends BaseApiAgent {

    /**
     * 璋冪敤鎺ュ彛鍥炶皟
     */
    private GetPushStateHandler handler;

    /**
     * HuaweiApiClient 杩炴帴缁撴灉鍥炶皟
     *
     * @param rst    缁撴灉鐮�
     * @param client HuaweiApiClient 瀹炰緥
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //闇�瑕佸湪瀛愮嚎绋嬩腑鎵ц鑾峰彇push鐘舵�佺殑鎿嶄綔
        ThreadUtil.INST.excute(new Runnable() {
            @Override
            public void run() {
                if (client == null || !ApiClientMgr.INST.isConnect(client)) {
                    HMSAgentLog.e("client not connted");
                    onGetPushStateResult(rst);
                } else {
                    HuaweiPush.HuaweiPushApi.getPushState(client);
                    onGetPushStateResult(HMSAgent.AgentResultCode.HMSAGENT_SUCCESS);
                }
            }
        });
    }

    void onGetPushStateResult(int rstCode) {
        HMSAgentLog.i("getPushState:callback=" + StrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 鑾峰彇push鐘舵�侊紝push鐘舵�佺殑鍥炶皟閫氳繃骞挎挱鍙戦�併��
     * 瑕佺洃鍚殑骞挎挱锛岃鍙傝HMS-SDK寮�鍙戝噯澶囦腑PushReceiver鐨勬敞鍐�
     */
    public void getPushState(GetPushStateHandler handler) {
        HMSAgentLog.i("getPushState:handler=" + StrUtils.objDesc(handler));
        this.handler = handler;
        connect();
    }
}
