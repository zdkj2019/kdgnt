package com.huawei.android.hms.agent.push;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.CallbackCodeRunnable;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.StrUtils;
import com.huawei.android.hms.agent.common.ThreadUtil;
import com.huawei.android.hms.agent.push.handler.DeleteTokenHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

/**
 * 鍒犻櫎pushtoken鐨勬帴鍙ｃ��
 */
public class DeleteTokenApi extends BaseApiAgent {

    /**
     * 寰呭垹闄ょ殑push token
     */
    private String token;

    /**
     * 璋冪敤鎺ュ彛鍥炶皟
     */
    private DeleteTokenHandler handler;

    /**
     * HuaweiApiClient 杩炴帴缁撴灉鍥炶皟
     *
     * @param rst    缁撴灉鐮�
     * @param client HuaweiApiClient 瀹炰緥
     */
    @Override
    public void onConnect(final int rst, final HuaweiApiClient client) {
        //闇�瑕佸湪瀛愮嚎绋嬩腑鎵ц鍒犻櫎TOKEN鎿嶄綔
        ThreadUtil.INST.excute(new Runnable() {
            @Override
            public void run() {
                //璋冪敤鍒犻櫎TOKEN闇�瑕佷紶鍏ラ�氳繃getToken鎺ュ彛鑾峰彇鍒癟OKEN锛屽苟涓旈渶瑕佸TOKEN杩涜闈炵┖鍒ゆ柇
                if (!TextUtils.isEmpty(token)){
                    if (client == null || !ApiClientMgr.INST.isConnect(client)) {
                        HMSAgentLog.e("client not connted");
                        onDeleteTokenResult(rst);
                    } else {
                        try {
                            HuaweiPush.HuaweiPushApi.deleteToken(client, token);
                            onDeleteTokenResult(HMSAgent.AgentResultCode.HMSAGENT_SUCCESS);
                        } catch (Exception e) {
                            HMSAgentLog.e("鍒犻櫎TOKEN澶辫触:" + e.getMessage());
                            onDeleteTokenResult(HMSAgent.AgentResultCode.CALL_EXCEPTION);
                        }
                    }
                } else {
                    HMSAgentLog.e("鍒犻櫎TOKEN澶辫触: 瑕佸垹闄ょ殑token涓虹┖");
                    onDeleteTokenResult(HMSAgent.AgentResultCode.EMPTY_PARAM);
                }
            }
        });
    }

    void onDeleteTokenResult(int rstCode) {
        HMSAgentLog.i("deleteToken:callback=" + StrUtils.objDesc(handler) +" retCode=" + rstCode);
        if (handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(handler, rstCode));
            handler = null;
        }
    }

    /**
     * 鍒犻櫎鎸囧畾鐨刾ushtoken
     * 璇ユ帴鍙ｅ彧鍦‥MUI5.1浠ュ強鏇撮珮鐗堟湰鐨勫崕涓烘墜鏈轰笂璋冪敤璇ユ帴鍙ｅ悗鎵嶄笉浼氭敹鍒癙USH娑堟伅銆�
     * @param token 瑕佸垹闄ょ殑token
     */
    public void deleteToken(String token, DeleteTokenHandler handler) {
        HMSAgentLog.i("deleteToken:token:" + StrUtils.objDesc(token) + " handler=" + StrUtils.objDesc(handler));
        this.token = token;
        this.handler = handler;
        connect();
    }
}