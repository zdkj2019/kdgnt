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
import com.huawei.android.hms.agent.push.handler.QueryAgreementHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

/**
 * 鑾峰彇push鍗忚灞曠ず鐨勬帴鍙ｃ��
 */
public class QueryAgreementApi extends BaseApiAgent {

    /**
     * 璋冪敤鎺ュ彛鍥炶皟
     */
    private QueryAgreementHandler handler;

    /**
     * HuaweiApiClient 杩炴帴缁撴灉鍥炶皟
     *
     * @param rst    缁撴灉鐮�
     * @param client HuaweiApiClient 瀹炰緥
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //闇�瑕佸湪瀛愮嚎绋嬩腑鎵ц鑾峰彇push鍗忚灞曠ず鐨勬搷浣�
        ThreadUtil.INST.excute(new Runnable() {
            @Override
            public void run() {
                if (client == null || !ApiClientMgr.INST.isConnect(client)) {
                    HMSAgentLog.e("client not connted");
                    onQueryAgreementResult(rst);
                } else {
                    HuaweiPush.HuaweiPushApi.queryAgreement(client);
                    onQueryAgreementResult(HMSAgent.AgentResultCode.HMSAGENT_SUCCESS);
                }
            }
        });
    }

    void onQueryAgreementResult(int rstCode) {
        HMSAgentLog.i("queryAgreement:callback=" + StrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 璇锋眰push鍗忚灞曠ず
     */
    public void queryAgreement(QueryAgreementHandler handler) {
        HMSAgentLog.i("queryAgreement:handler=" + StrUtils.objDesc(handler));
        this.handler = handler;
        connect();
    }
}
