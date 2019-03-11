package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.hms.activity.BridgeActivity;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

import java.util.ArrayList;
import java.util.List;


/**
 * Huawei Api Client 绠＄悊绫� | Huawei API Client Management class
 * 璐熻矗HuaweiApiClient鐨勮繛鎺ワ紝寮傚父澶勭悊绛� | Responsible for huaweiapiclient connection, exception handling, etc.
 */
public final class ApiClientMgr implements HuaweiApiClient.ConnectionCallbacks, HuaweiApiClient.OnConnectionFailedListener, IActivityResumeCallback, IActivityPauseCallback, IActivityDestroyedCallback {

    /**
     * 鍗曞疄渚�
     */
    public static final ApiClientMgr INST = new ApiClientMgr();

    /**
     * 搴旂敤甯傚満鍖呭悕
     */
    private static final String PACKAGE_NAME_HIAPP = "com.huawei.appmarket";

    /**
     * 鍥炶皟閿侊紝閬垮厤杩炴帴鍥炶皟绱婁贡
     */
    private static final Object CALLBACK_LOCK = new Object();

    /**
     * 闈欐�佹敞鍐屽洖璋冮攣锛岄伩鍏嶆敞鍐屽拰鍥炶皟绱婁贡
     */
    private static final Object STATIC_CALLBACK_LOCK = new Object();

    /**
     * client鎿嶄綔閿侊紝閬垮厤杩炴帴浣跨敤绱婁贡
     */
    private static final Object APICLIENT_LOCK = new Object();

    /**
     * api client 杩炴帴瓒呮椂
     */
    private static final int APICLIENT_CONNECT_TIMEOUT = 30000;

    /**
     * 瑙ｅ喅鍗囩骇閿欒鏃禷ctivity onResume 绋冲畾鍦�3绉掓椂闂村垽鏂瑽ridgeActivity涓婇潰鏈夋病鏈夊叾浠朼ctivity
     */
    private static final int UPDATE_OVER_ACTIVITY_CHECK_TIMEOUT = 3000;

    /**
     * api client 瑙ｅ喅閿欒鎷夎捣鐣岄潰瓒呮椂
     */
    private static final int APICLIENT_STARTACTIVITY_TIMEOUT = 3000;

    /**
     * client 杩炴帴瓒呮椂娑堟伅
     */
    private static final int APICLIENT_TIMEOUT_HANDLE_MSG = 3;

    /**
     * client 鎷夎捣activity瓒呮椂娑堟伅
     */
    private static final int APICLIENT_STARTACTIVITY_TIMEOUT_HANDLE_MSG = 4;

    /**
     * 瑙ｅ喅鍗囩骇閿欒鏃禷ctivity onResume 绋冲畾鍦�3绉掓椂闂村垽鏂瑽ridgeActivity涓婇潰鏈夋病鏈夊叾浠朼ctivity
     */
    private static final int UPDATE_OVER_ACTIVITY_CHECK_TIMEOUT_HANDLE_MSG = 5;

    /**
     * 鏈�澶у皾璇曡繛鎺ユ鏁�
     */
    private static final int MAX_RESOLVE_TIMES = 3;

    /**
     * 涓婁笅鏂囷紝鐢ㄦ潵澶勭悊杩炴帴澶辫触
     */
    private Context context;

    /**
     * 褰撳墠搴旂敤鍖呭悕
     */
    private String curAppPackageName;

    /**
     * HuaweiApiClient 瀹炰緥
     */
    private HuaweiApiClient apiClient;

    /**
     * 鏄惁鍏佽瑙ｅ喅connect閿欒锛堣В鍐砪onnect閿欒闇�瑕佹媺璧穉ctivity锛�
     */
    private boolean allowResolveConnectError = false;

    /**
     * 鏄惁姝ｅ湪瑙ｅ喅杩炴帴閿欒
     */
    private boolean isResolving;

    /**
     * HMSSDK 瑙ｅ喅閿欒鐨刟ctivity
     */
    private BridgeActivity resolveActivity;

    /**
     * 鏄惁瀛樺湪鍏朵粬activity瑕嗙洊鍦ㄥ崌绾ctivity涔嬩笂
     */
    private boolean hasOverActivity = false;

    /**
     * 褰撳墠鍓╀綑灏濊瘯娆℃暟
     */
    private int curLeftResolveTimes = MAX_RESOLVE_TIMES;

    /**
     * 杩炴帴鍥炶皟
     */
    private List<IClientConnectCallback> connCallbacks = new ArrayList<IClientConnectCallback>();

    /**
     * 娉ㄥ唽鐨勯潤鎬佸洖璋�
     */
    private List<IClientConnectCallback> staticCallbacks = new ArrayList<IClientConnectCallback>();

    /**
     * 瓒呮椂handler鐢ㄦ潵澶勭悊client connect 瓒呮椂
     */
    private Handler timeoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            boolean hasConnCallbacks;
            synchronized (CALLBACK_LOCK) {
                hasConnCallbacks = !connCallbacks.isEmpty();
            }

            if (msg != null && msg.what == APICLIENT_TIMEOUT_HANDLE_MSG && hasConnCallbacks) {
                HMSAgentLog.d("connect time out");
                resetApiClient();
                onConnectEnd(HMSAgent.AgentResultCode.APICLIENT_TIMEOUT);
                return true;
            } else if (msg != null && msg.what == APICLIENT_STARTACTIVITY_TIMEOUT_HANDLE_MSG && hasConnCallbacks) {
                HMSAgentLog.d("start activity time out");
                onConnectEnd(HMSAgent.AgentResultCode.APICLIENT_TIMEOUT);
                return true;
            } else if (msg != null && msg.what == UPDATE_OVER_ACTIVITY_CHECK_TIMEOUT_HANDLE_MSG && hasConnCallbacks) {
                HMSAgentLog.d("Discarded update dispose:hasOverActivity=" +hasOverActivity + " resolveActivity=" + StrUtils.objDesc(resolveActivity));
                if (hasOverActivity && resolveActivity != null && !resolveActivity.isFinishing()) {
                    onResolveErrorRst(ConnectionResult.CANCELED);
                }
                return true;
            }
            return false;
        }
    });

    /**
     * 绉佹湁鏋勯�犳柟娉�
     */
    private ApiClientMgr () {
    }

    /**
     * 鍒濆鍖�
     * @param app 搴旂敤绋嬪簭
     */
    public void init(Application app) {

        HMSAgentLog.d("init");

        // 淇濆瓨搴旂敤绋嬪簭context
        context = app.getApplicationContext();

        // 鍙栧緱搴旂敤绋嬪簭鍖呭悕
        curAppPackageName = app.getPackageName();

        // 娉ㄥ唽activity onResume鍥炶皟
        ActivityMgr.INST.unRegisterActivitResumeEvent(this);
        ActivityMgr.INST.registerActivitResumeEvent(this);

        // 娉ㄥ唽activity onPause鍥炶皟
        ActivityMgr.INST.unRegisterActivitPauseEvent(this);
        ActivityMgr.INST.registerActivitPauseEvent(this);

        // 娉ㄥ唽activity onDestroyed 鍥炶皟
        ActivityMgr.INST.unRegisterActivitDestroyedEvent(this);
        ActivityMgr.INST.registerActivitDestroyedEvent(this);
    }

    /**
     * 鏂紑apiclient锛屼竴鑸笉闇�瑕佽皟鐢�
     */
    public void release() {
        HMSAgentLog.d("release");

        isResolving = false;
        resolveActivity = null;
        hasOverActivity = false;

        HuaweiApiClient client =  getApiClient();
        if (client != null) {
            client.disconnect();
            synchronized (APICLIENT_LOCK) {
                apiClient = null;
            }
        }

        synchronized (STATIC_CALLBACK_LOCK) {
            staticCallbacks.clear();
        }

        synchronized (CALLBACK_LOCK) {
            connCallbacks.clear();
        }
    }

    /**
     * 鑾峰彇褰撳墠鐨� HuaweiApiClient
     * @return HuaweiApiClient 瀹炰緥
     */
    public HuaweiApiClient getApiClient() {
        synchronized (APICLIENT_LOCK) {
            return apiClient;
        }
    }

    /**
     * 鍒ゆ柇client鏄惁宸茬粡杩炴帴
     * @param client 瑕佹娴嬬殑client
     * @return 鏄惁宸茬粡杩炴帴
     */
    public boolean isConnect(HuaweiApiClient client) {
        return client != null && client.isConnected();
    }

    /**
     * 娉ㄥ唽apiclient杩炴帴浜嬩欢
     * @param staticCallback 杩炴帴鍥炶皟
     */
    public void registerClientConnect(IClientConnectCallback staticCallback){
        synchronized (STATIC_CALLBACK_LOCK) {
            staticCallbacks.add(staticCallback);
        }
    }

    /**
     * 鍙嶆敞鍐宎piclient杩炴帴浜嬩欢
     * @param staticCallback 杩炴帴鍥炶皟
     */
    public void removeClientConnectCallback(IClientConnectCallback staticCallback) {
        synchronized (STATIC_CALLBACK_LOCK) {
            staticCallbacks.remove(staticCallback);
        }
    }

    /**
     * 閲嶆柊鍒涘缓apiclient
     * 2绉嶆儏鍐甸渶瑕侀噸鏂板垱寤猴細1銆侀娆� 2銆乧lient鐨勭姸鎬佸凡缁忕磰涔�
     * @return 鏂板垱寤虹殑client
     */
    private HuaweiApiClient resetApiClient(){
        synchronized (APICLIENT_LOCK) {
            if (apiClient != null) {
                // 瀵逛簬鑰佺殑apiClient锛�1鍒嗛挓鍚庢墠涓㈠純锛岄槻姝㈠闈㈡鍦ㄤ娇鐢ㄨ繃绋嬩腑杩欒竟disConnect浜�
                disConnectClientDelay(apiClient, 60000);
            }

            HMSAgentLog.d("reset client");

            // 杩欑閲嶇疆client锛屾瀬绔儏鍐靛彲鑳戒細鍑虹幇2涓猚lient閮藉洖璋冪粨鏋滅殑鎯呭喌銆傛鏃跺彲鑳藉嚭鐜皉stCode=0锛屼絾鏄痗lient鏃犳晥銆�
            // 鍥犱负涓氬姟璋冪敤灏佽涓兘杩涜浜嗕竴娆￠噸璇曘�傛墍浠ヤ笉浼氭湁闂
            apiClient = new HuaweiApiClient.Builder(context)
                    .addApi(HuaweiPush.PUSH_API)
                    .addConnectionCallbacks(INST)
                    .addOnConnectionFailedListener(INST)
                    .build();
            return apiClient;
        }
    }

    /**
     * 杩炴帴 HuaweiApiClient,
     * @param callback 杩炴帴缁撴灉鍥炶皟锛屼竴瀹氫笉鑳戒负null,鍦ㄥ瓙绾跨▼杩涜鍥炶皟
     * @param allowResolve 鏄惁鍏佽瑙ｅ喅閿欒锛岃В鍐抽敊璇椂鍙兘浼氭媺璧风晫闈�
     */
    public void connect(IClientConnectCallback callback, boolean allowResolve) {

        if (context == null) {
            aSysnCallback(HMSAgent.AgentResultCode.HMSAGENT_NO_INIT, callback);
            return;
        }

        HuaweiApiClient client =  getApiClient();
        // client 鏈夋晥锛屽垯鐩存帴鍥炶皟
        if (client != null && client.isConnected()) {
            HMSAgentLog.d("client is valid");
            aSysnCallback(HMSAgent.AgentResultCode.HMSAGENT_SUCCESS, callback);
            return;
        } else {
            // client鏃犳晥锛屽皢callback鍔犲叆闃熷垪锛屽苟鍚姩杩炴帴
            synchronized (CALLBACK_LOCK) {
                HMSAgentLog.d("client is invalid锛歴ize=" + connCallbacks.size());
                allowResolveConnectError = allowResolveConnectError || allowResolve;
                if (connCallbacks.isEmpty()) {
                    connCallbacks.add(callback);

                    // 杩炴帴灏濊瘯鏈�澶ф鏁�
                    curLeftResolveTimes = MAX_RESOLVE_TIMES;

                    startConnect();
                } else {
                    connCallbacks.add(callback);
                }
            }
        }
    }

    /**
     * 绾跨▼涓繘琛孒uawei Api Client 鐨勮繛鎺�
     */
    private void startConnect() {

        // 瑙﹀彂涓�娆¤繛鎺ュ皢閲嶈瘯娆℃暟鍑�1
        curLeftResolveTimes--;

        HMSAgentLog.d("start thread to connect");
        ThreadUtil.INST.excute(new Runnable() {
            @Override
            public void run() {
                HuaweiApiClient client =  getApiClient();
                if (client == null) {
                    HMSAgentLog.d("create client");
                    client = resetApiClient();
                }

                HMSAgentLog.d("connect");
                Activity curActivity = ActivityMgr.INST.getLastActivity();

                // 鑰冭檻鍒版湁cp鍚庡彴闇�瑕佽皟鐢ㄦ帴鍙ｏ紝HMSSDK鍘绘帀浜哸ctivity涓嶈兘涓虹┖鐨勫垽鏂�傝繖閲屽彧鏄彇褰撳墠activity锛屽彲鑳戒负绌�
                timeoutHandler.sendEmptyMessageDelayed(APICLIENT_TIMEOUT_HANDLE_MSG, APICLIENT_CONNECT_TIMEOUT);
                client.connect(curActivity);
            }
        });
    }

    /**
     * Huawei Api Client 杩炴帴缁撴潫鏂规硶
     * @param rstCode client 杩炴帴缁撴灉鐮�
     */
    private void onConnectEnd(final int rstCode) {
        HMSAgentLog.d("connect end:" + rstCode);

        synchronized (CALLBACK_LOCK) {
            // 鍥炶皟鍚勪釜鍥炶皟鎺ュ彛杩炴帴缁撴潫
            for (IClientConnectCallback callback : connCallbacks) {
                aSysnCallback(rstCode, callback);
            }
            connCallbacks.clear();

            // 鎭㈠榛樿涓嶆樉绀�
            allowResolveConnectError = false;
        }

        synchronized (STATIC_CALLBACK_LOCK) {
            // 鍥炶皟鍚勪釜鍥炶皟鎺ュ彛杩炴帴缁撴潫
            for (IClientConnectCallback callback : staticCallbacks) {
                aSysnCallback(rstCode, callback);
            }
            staticCallbacks.clear();
        }
    }

    /**
     * 璧风嚎绋嬪洖璋冨悇涓帴鍙ｏ紝閬垮厤鍏朵腑涓�涓洖璋冭�呰�楁椂闀垮奖鍝嶅叾浠栬皟鐢ㄨ��
     * @param rstCode 缁撴灉鐮�
     * @param callback 鍥炶皟
     */
    private void aSysnCallback(final int rstCode, final IClientConnectCallback callback) {
        ThreadUtil.INST.excute(new Runnable() {
            @Override
            public void run() {
                HuaweiApiClient client =  getApiClient();
                HMSAgentLog.d("callback connect: rst=" + rstCode + " apiClient=" + client);
                callback.onConnect(rstCode, client);
            }
        });
    }

    /**
     * Activity onResume鍥炶皟
     *
     * @param activity 鍙戠敓 onResume 浜嬩欢鐨刟ctivity
     */
    @Override
    public void onActivityResume(Activity activity) {
        // 閫氱煡hmssdk activity onResume浜�
        if (apiClient != null) {
            HMSAgentLog.d("tell hmssdk: onResume");
            apiClient.onResume(activity);
        }

        // 濡傛灉姝ｅ湪瑙ｅ喅閿欒锛屽垯澶勭悊琚鐩栫殑鍦烘櫙
        HMSAgentLog.d("is resolving:" + isResolving);
        if (isResolving && !PACKAGE_NAME_HIAPP.equals(curAppPackageName)) {
            if (activity instanceof BridgeActivity) {
                resolveActivity = (BridgeActivity)activity;
                hasOverActivity = false;
                HMSAgentLog.d("received bridgeActivity:" + StrUtils.objDesc(resolveActivity));
            } else if (resolveActivity != null && !resolveActivity.isFinishing()){
                hasOverActivity = true;
                HMSAgentLog.d("received other Activity:" + StrUtils.objDesc(resolveActivity));
            }
            timeoutHandler.removeMessages(UPDATE_OVER_ACTIVITY_CHECK_TIMEOUT_HANDLE_MSG);
            timeoutHandler.sendEmptyMessageDelayed(UPDATE_OVER_ACTIVITY_CHECK_TIMEOUT_HANDLE_MSG, UPDATE_OVER_ACTIVITY_CHECK_TIMEOUT);
        }
    }

    /**
     * Activity onPause鍥炶皟
     *
     * @param activity 鍙戠敓 onPause 浜嬩欢鐨刟ctivity
     */
    @Override
    public void onActivityPause(Activity activity) {
        // 閫氱煡hmssdk锛宎ctivity onPause浜�
        if (apiClient != null) {
            apiClient.onPause(activity);
        }
    }

    /**
     * Activity onPause鍥炶皟
     *
     * @param activityDestroyed 鍙戠敓 onDestroyed 浜嬩欢鐨刟ctivity
     * @param activityNxt       涓嬩釜瑕佹樉绀虹殑activity
     */
    @Override
    public void onActivityDestroyed(Activity activityDestroyed, Activity activityNxt) {
        if (activityNxt == null) {
            // 鎵�鏈塧ctivity閿�姣佸悗锛岄噸缃甤lient锛屽惁鍒欏叕鍛婄殑鏍囧織浣嶈繕鍦紝涓嬫寮逛笉鍑烘潵
            resetApiClient();
        }
    }

    /**
     * connect fail 瑙ｅ喅缁撴灉鍥炶皟锛� 鐢� HMSAgentActivity 鍦� onActivityResult 涓皟鐢�
     * @param result 瑙ｅ喅缁撴灉
     */
    void onResolveErrorRst(int result) {
        HMSAgentLog.d("result="+result);
        isResolving = false;
        resolveActivity = null;
        hasOverActivity = false;

        if(result == ConnectionResult.SUCCESS) {
            HuaweiApiClient client =  getApiClient();
            if (!client.isConnecting() && !client.isConnected() && curLeftResolveTimes > 0) {
                startConnect();
                return;
            }
        }

        onConnectEnd(result);
    }

    /**
     * HMSAgentActivity 鎷夎捣鎷変簡锛堣蛋浜唎nCreate锛�
     */
    void onActivityLunched(){
        HMSAgentLog.d("resolve onActivityLunched");
        // 鎷夎捣鐣岄潰鍥炶皟锛岀Щ闄ゆ媺璧风晫闈㈣秴鏃�
        timeoutHandler.removeMessages(APICLIENT_STARTACTIVITY_TIMEOUT_HANDLE_MSG);
        isResolving = true;
    }

    /**
     * Huawe Api Client 杩炴帴鎴愬姛鍥炲埌
     */
    @Override
    public void onConnected() {
        HMSAgentLog.d("connect success");
        timeoutHandler.removeMessages(APICLIENT_TIMEOUT_HANDLE_MSG);
        onConnectEnd(ConnectionResult.SUCCESS);
    }

    /**
     * 褰揷lient鍙樻垚鏂紑鐘舵�佹椂浼氳璋冪敤銆傝繖鏈夊彲鑳藉彂鐢熷湪杩滅▼鏈嶅姟鍑虹幇闂鏃讹紙渚嬪锛氬嚭鐜癱rash鎴栬祫婧愰棶棰樺鑷存湇鍔¤绯荤粺鏉�鎺夛級銆�
     * 褰撹璋冪敤鏃讹紝鎵�鏈夌殑璇锋眰閮戒細琚彇娑堬紝浠讳綍listeners閮戒笉浼氳鎵ц銆傞渶瑕� CP 寮�鍙戜唬鐮佸皾璇曟仮澶嶈繛鎺ワ紙connect锛夈��
     * 搴旂敤绋嬪簭搴旇绂佺敤闇�瑕佹湇鍔＄殑鐩稿叧UI缁勪欢锛岀瓑寰厈@link #onConnected()} 鍥炶皟鍚庨噸鏂板惎鐢ㄤ粬浠��<br>
     *
     * @param cause 鏂紑鐨勫師鍥�. 甯搁噺瀹氫箟锛� CAUSE_*.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        HMSAgentLog.d("connect suspended");
        connect(new EmptyConnectCallback("onConnectionSuspended try end:"), true);
    }

    /**
     * 寤虹珛client鍒皊ervice鐨勮繛鎺ュけ璐ユ椂璋冪敤
     *
     * @param result 杩炴帴缁撴灉锛岀敤浜庤В鍐抽敊璇拰鐭ラ亾浠�涔堢被鍨嬬殑閿欒
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        timeoutHandler.removeMessages(APICLIENT_TIMEOUT_HANDLE_MSG);

        if (result == null) {
            HMSAgentLog.e("result is null");
            onConnectEnd(HMSAgent.AgentResultCode.RESULT_IS_NULL);
            return;
        }

        int errCode = result.getErrorCode();
        HMSAgentLog.d("errCode=" + errCode + " allowResolve=" + allowResolveConnectError);

        if(HuaweiApiAvailability.getInstance().isUserResolvableError(errCode) && allowResolveConnectError) {
            Activity activity = ActivityMgr.INST.getLastActivity();
            if (activity != null) {
                try {
                    timeoutHandler.sendEmptyMessageDelayed(APICLIENT_STARTACTIVITY_TIMEOUT_HANDLE_MSG, APICLIENT_STARTACTIVITY_TIMEOUT);
                    Intent intent = new Intent(activity, HMSAgentActivity.class);
                    intent.putExtra(HMSAgentActivity.CONN_ERR_CODE_TAG, errCode);
                    activity.startActivity(intent);
                    return;
                } catch (Exception e) {
                    HMSAgentLog.e("start HMSAgentActivity exception:" + e.getMessage());
                    timeoutHandler.removeMessages(APICLIENT_STARTACTIVITY_TIMEOUT_HANDLE_MSG);
                    onConnectEnd(HMSAgent.AgentResultCode.START_ACTIVITY_ERROR);
                    return;
                }
            } else {
                // 褰撳墠娌℃湁鐣岄潰澶勭悊涓嶄簡閿欒
                HMSAgentLog.d("no activity");
                onConnectEnd(HMSAgent.AgentResultCode.NO_ACTIVITY_FOR_USE);
                return;
            }
        } else {
            //鍏朵粬閿欒鐮佺洿鎺ラ�忎紶
        }

        onConnectEnd(errCode);
    }

    private static void disConnectClientDelay(final HuaweiApiClient clientTmp, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clientTmp.disconnect();
            }
        }, delay);
    }
}
