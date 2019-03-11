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
import com.huawei.android.hms.agent.push.handler.EnableReceiveNotifyMsgHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

/**
 * 鎵撳紑鑷憟鐜版秷鎭紑鍏崇殑鎺ュ彛銆�
 */
public class EnableReceiveNotifyMsgApi extends BaseApiAgent {

    /**
     * 鏄惁鎵撳紑寮�鍏�
     */
    boolean enable;

    /**
     * 璋冪敤鎺ュ彛鍥炶皟
     */
    private EnableReceiveNotifyMsgHandler handler;

    /**
     * HuaweiApiClient 杩炴帴缁撴灉鍥炶皟
     *
     * @param rst    缁撴灉鐮�
     * @param client HuaweiApiClient 瀹炰緥
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //闇�瑕佸湪瀛愮嚎绋嬩腑鎵ц寮�鍏崇殑鎿嶄綔
        ThreadUtil.INST.excute(new Runnable() {
            @Override
            public void run() {
                if (client == null || !ApiClientMgr.INST.isConnect(client)) {
                    HMSAgentLog.e("client not connted");
                    onEnableReceiveNotifyMsgResult(rst);
                } else {
                    // 寮�鍚�/鍏抽棴鑷憟鐜版秷鎭�
                    HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(client, enable);
                    onEnableReceiveNotifyMsgResult(HMSAgent.AgentResultCode.HMSAGENT_SUCCESS);
                }
            }
        });
    }

    void onEnableReceiveNotifyMsgResult(int rstCode) {
        HMSAgentLog.i("enableReceiveNotifyMsg:callback=" + StrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 鎵撳紑/鍏抽棴鑷憟鐜版秷鎭�
     * @param enable 鎵撳紑/鍏抽棴
     */
    public void enableReceiveNotifyMsg(boolean enable, EnableReceiveNotifyMsgHandler handler) {
        HMSAgentLog.i("enableReceiveNotifyMsg:enable=" + enable + " handler=" + StrUtils.objDesc(handler));
        this.enable = enable;
        this.handler = handler;
        connect();
    }
}
