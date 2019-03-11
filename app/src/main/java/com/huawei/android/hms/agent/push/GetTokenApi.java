package com.huawei.android.hms.agent.push;

import android.os.Handler;
import android.os.Looper;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.CallbackCodeRunnable;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.StrUtils;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.entity.core.CommonCode;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;

/**
 * 鑾峰彇token鐨刾ush鎺ュ彛锛宼oken鐨勭粨鏋滈�氳繃骞挎挱杩涜鎺ユ敹銆�
 */
public class GetTokenApi extends BaseApiAgent {

    /**
     * client 鏃犳晥鏈�澶у皾璇曟鏁�
     */
    private static final int MAX_RETRY_TIMES = 1;

    /**
     * 缁撴灉鍥炶皟
     */
    private GetTokenHandler handler;

    /**
     * 褰撳墠鍓╀綑閲嶈瘯娆℃暟
     */
    private int retryTimes = MAX_RETRY_TIMES;

    /**
     * HuaweiApiClient 杩炴帴缁撴灉鍥炶皟
     *
     * @param rst    缁撴灉鐮�
     * @param client HuaweiApiClient 瀹炰緥
     */
    @Override
    public void onConnect(int rst, HuaweiApiClient client) {
        if (client == null || !ApiClientMgr.INST.isConnect(client)) {
            HMSAgentLog.e("client not connted");
            onPushTokenResult(rst, null);
            return;
        }

        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {
            @Override
            public void onResult(TokenResult result) {
                if (result == null) {
                    HMSAgentLog.e("result is null");
                    onPushTokenResult(HMSAgent.AgentResultCode.RESULT_IS_NULL, null);
                    return;
                }

                Status status = result.getStatus();
                if (status == null) {
                    HMSAgentLog.e("status is null");
                    onPushTokenResult(HMSAgent.AgentResultCode.STATUS_IS_NULL, null);
                    return;
                }

                int rstCode = status.getStatusCode();
                HMSAgentLog.d("rstCode=" + rstCode);
                // 闇�瑕侀噸璇曠殑閿欒鐮侊紝骞朵笖鍙互閲嶈瘯
                if ((rstCode == CommonCode.ErrorCode.SESSION_INVALID
                        || rstCode == CommonCode.ErrorCode.CLIENT_API_INVALID) && retryTimes > 0) {
                    retryTimes--;
                    connect();
                } else {
                    onPushTokenResult(rstCode, result);
                }
            }
        });
    }

    /**
     * 鑾峰彇pushtoken鎺ュ彛璋冪敤鍥炶皟
     * pushtoken閫氳繃骞挎挱涓嬪彂锛岃鐩戝惉鐨勫箍鎾紝璇峰弬瑙丠MS-SDK寮�鍙戝噯澶囦腑PushReceiver鐨勬敞鍐�
     * @param rstCode 缁撴灉鐮�
     * @param result 璋冪敤鑾峰彇pushtoken鎺ュ彛鐨勭粨鏋�
     */
    void onPushTokenResult(int rstCode, TokenResult result) {
        HMSAgentLog.i("getToken:callback=" + StrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
        retryTimes = MAX_RETRY_TIMES;
    }

    /**
     * 鑾峰彇pushtoken鎺ュ彛
     * pushtoken閫氳繃骞挎挱涓嬪彂锛岃鐩戝惉鐨勫箍鎾紝璇峰弬瑙丠MS-SDK寮�鍙戝噯澶囦腑PushReceiver鐨勬敞鍐�
     * @param handler pushtoken鎺ュ彛璋冪敤鍥炶皟
     */
    public void getToken(GetTokenHandler handler) {
        HMSAgentLog.i("getToken:handler=" + StrUtils.objDesc(handler));
        this.handler = handler;
        retryTimes = MAX_RETRY_TIMES;
        connect();
    }
}
