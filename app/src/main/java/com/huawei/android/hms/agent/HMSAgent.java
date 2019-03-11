package com.huawei.android.hms.agent;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.huawei.android.hms.agent.common.ActivityMgr;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.INoProguard;
import com.huawei.android.hms.agent.common.IClientConnectCallback;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.DeleteTokenApi;
import com.huawei.android.hms.agent.push.EnableReceiveNormalMsgApi;
import com.huawei.android.hms.agent.push.EnableReceiveNotifyMsgApi;
import com.huawei.android.hms.agent.push.GetPushStateApi;
import com.huawei.android.hms.agent.push.GetTokenApi;
import com.huawei.android.hms.agent.push.QueryAgreementApi;
import com.huawei.android.hms.agent.push.handler.DeleteTokenHandler;
import com.huawei.android.hms.agent.push.handler.EnableReceiveNormalMsgHandler;
import com.huawei.android.hms.agent.push.handler.EnableReceiveNotifyMsgHandler;
import com.huawei.android.hms.agent.push.handler.GetPushStateHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.huawei.android.hms.agent.push.handler.QueryAgreementHandler;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;

/**
 * HMSAgent 灏佽鍏ュ彛绫汇�� 鎻愪緵浜咹MS SDK 鍔熻兘鐨勫皝瑁咃紝浣垮紑鍙戣�呮洿鑱氱劍涓氬姟鐨勫鐞嗐��
 * HMSAgent encapsulates the entry class. Provides a encapsulation of the HMS SDK functionality that enables developers to focus more on business processing.
 */
public final class HMSAgent implements INoProguard {

    /**
     * 鍩虹鐗堟湰 | Base version
     */
    private static final String VER_020503001 = "020503001";

    /**
     * 2.6.0 鐗堟湰1                                            | 2.6.0 version 1
     * 瀵瑰锛氭帴鍙ｄ笉鍙�                                         | External: interface unchanged
     * 瀵瑰唴锛欻MSSDK connect 鎺ュ彛澧炲姞activity鍙傛暟              | Internal: HMSSDK connect interface to increase activity parameters
     *      HMSSDK sign 鎺ュ彛澧炲姞activity鍙傛暟                      | HMSSDK sign interface to increase activity parameters
     * 鑷韩浼樺寲锛�                                             | Self optimization:
     *      1銆佸鍔犱簡鍗囩骇鏃惰鍏朵粬鐣岄潰瑕嗙洊鐨勫鐞�                  | Increased handling of other interface coverage issues when upgrading
     *      2銆乬ame妯″潡savePlayerInfo鎺ュ彛锛屽幓鎺塧ctivity鐨勫垽鏂�    | Game Module Saveplayerinfo method to remove activity judgments
     *      3銆佽В鍐抽敊璇洖璋冩垚鍔燂紝澧炲姞閲嶈瘯娆℃暟3娆�                 | Resolve error callback succeeded, increase retry count 3 times
	 *      4銆佹彁渚涗簡澶氱HMSAgent鍒濆鍖栨柟娉�                      | Provides a variety of hmsagent initialization methods
     *      5銆佸垵濮嬪寲鏃跺鍔犱簡鐗堟湰鍙锋牎楠�                          | Increased version number checksum during initialization
     */
    private static final String VER_020600001 = "020600001";

    /**
     * 2.6.0.200                                         | 2.6.0.200
     * 鑷韩浼樺寲锛�                                        | Self optimization:
     *      1銆佸鍔爏hell鑴氭湰鐢ㄦ潵鎶藉彇浠ｇ爜鍜岀紪璇戞垚jar            | Add shell script to extract code and compile into jar
     *      2銆佺ず渚嬩腑manifest閲岄潰鍗囩骇閰嶇疆閿欒淇              | Example manifest upgrade configuration error Repair
     *      3銆佹娊鍙栦唬鐮佷腑鍘绘帀manifest鏂囦欢锛屽彧鐣欑函浠ｇ爜          | Remove manifest files in the extraction code, leaving only pure code
     */
    private static final String VER_020600200 = "020600200";

    /**
     * 2.6.0.302                                         | 2.6.0.302
     * 淇敼manifest锛屽垹闄ms鐗堟湰鍙烽厤缃紱澧炲姞鐩存帴浼犲叆璇锋眰鍜岀閽ョ殑绛惧悕鏂规硶灏佽
     */
    private static final String VER_020600302 = "020600302";

    /**
     * 褰撳墠鐗堟湰鍙� | Current version number
     */
    public static final String CURVER = VER_020600302;



    public static final class AgentResultCode {

        /**
         * HMSAgent 鎴愬姛 | success
         */
        public static final int HMSAGENT_SUCCESS = 0;

        /**
         * HMSAgent 娌℃湁鍒濆鍖� | Hmsagent not initialized
         */
        public static final int HMSAGENT_NO_INIT = -1000;

        /**
         * 璇锋眰闇�瑕乤ctivity锛屼絾褰撳墠娌℃湁鍙敤鐨刟ctivity | Request requires activity, but no active activity is currently available
         */
        public static final int NO_ACTIVITY_FOR_USE = -1001;

        /**
         * 缁撴灉涓虹┖ | Result is empty
         */
        public static final int RESULT_IS_NULL = -1002;

        /**
         * 鐘舵�佷负绌� | Status is empty
         */
        public static final int STATUS_IS_NULL = -1003;

        /**
         * 鎷夎捣activity寮傚父锛岄渶瑕佹鏌ctivity鏈夋病鏈夊湪manifest涓厤缃� | Pull up an activity exception and need to check if the activity is configured in manifest
         */
        public static final int START_ACTIVITY_ERROR = -1004;

        /**
         * onActivityResult 鍥炶皟缁撴灉閿欒 | Onactivityresult Callback Result Error
         */
        public static final int ON_ACTIVITY_RESULT_ERROR = -1005;

        /**
         * 閲嶅璇锋眰 | Duplicate Request
         */
        public static final int REQUEST_REPEATED = -1006;

        /**
         * 杩炴帴client 瓒呮椂 | Connect Client Timeout
         */
        public static final int APICLIENT_TIMEOUT = -1007;

        /**
         * 璋冪敤鎺ュ彛寮傚父 | Calling an interface exception
         */
        public static final int CALL_EXCEPTION = -1008;

        /**
         * 鎺ュ彛鍙傛暟涓虹┖ | Interface parameter is empty
         */
        public static final int EMPTY_PARAM = -1009;
    }

    private HMSAgent(){}

    private static boolean checkSDKVersion(Context context){
        long sdkMainVerL = HuaweiApiAvailability.HMS_SDK_VERSION_CODE/1000;
        long agentMainVerL = Long.parseLong(CURVER)/1000;
        if (sdkMainVerL != agentMainVerL) {
            String errMsg = "error: HMSAgent major version code ("+agentMainVerL+") does not match HMSSDK major version code ("+sdkMainVerL+")";
            HMSAgentLog.e(errMsg);
            Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * 鍒濆鍖栨柟娉曪紝浼犲叆绗竴涓晫闈㈢殑activity   | Initialization method, passing in the first interface activity
     * @param activity 褰撳墠鐣岄潰             | Current interface
     * @return true锛氭垚鍔� false锛氬け璐�        | True: Success false: Failed
     */
    public static boolean init(Activity activity) {
        return init(null, activity);
    }

    /**
     * 鍒濆鍖栨柟娉曪紝寤鸿鍦ˋpplication onCreate閲岄潰璋冪敤    | Initialization method, it is recommended to call in creator OnCreate
     * @param app 搴旂敤绋嬪簭                              | Application
     * @return true锛氭垚鍔� false锛氬け璐�                   | True: Success false: Failed
     */
    public static boolean init(Application app) {
        return init(app, null);
    }

    /**
     * 鍒濆鍖栨柟娉曪紝寤鸿鍦ˋpplication onCreate閲岄潰璋冪敤 | Initialization method, it is recommended to call in creator OnCreate
     * @param app 搴旂敤绋嬪簭 | Application
     * @param activity 褰撳墠鐣岄潰 | Current activity
     * @return true锛氭垚鍔� false锛氬け璐� | True: Success false: Failed
     */
    public static boolean init(Application app, Activity activity) {

        Application appTmp = app;
        Activity activityTmp = activity;

        // 涓や釜鍙傛暟閮戒负null锛岀洿鎺ユ姏寮傚父 | Two parameters are null, throwing exceptions directly
        if (appTmp == null && activityTmp == null) {
            HMSAgentLog.e("the param of method HMSAgent.init can not be null !!!");
            return false;
        }

        // 濡傛灉application瀹炰緥涓簄ull锛屽垯浠巃ctivity閲岄潰鍙� | If the creator instance is null, it is taken from the activity
        if (appTmp == null) {
            appTmp = activityTmp.getApplication();
        }

        // 濡傛灉application瀹炰緥浠嶇劧涓簄ull锛屾姏寮傚父 | Throws an exception if the creator instance is still null
        if (appTmp == null) {
            HMSAgentLog.e("the param of method HMSAgent.init app can not be null !!!");
            return false;
        }

        // activity 宸茬粡澶辨晥锛屽垯璧嬪�糿ull | Assignment NULL if activity has been invalidated
        if (activityTmp != null && activityTmp.isFinishing()) {
            activityTmp = null;
        }

        // 妫�鏌MSAgent 鍜� HMSSDK 鐗堟湰鍖归厤鍏崇郴 | Check hmsagent and HMSSDK version matching relationships
        if (!checkSDKVersion(appTmp)) {
            return false;
        }

        HMSAgentLog.i("init HMSAgent " + CURVER + " with hmssdkver " + HuaweiApiAvailability.HMS_SDK_VERSION_CODE);

        // 鍒濆鍖朼ctivity绠＄悊绫� | Initializing Activity Management Classes
        ActivityMgr.INST.init(appTmp, activityTmp);

        // 鍒濆鍖朒uaweiApiClient绠＄悊绫� | Initialize Huaweiapiclient Management class
        ApiClientMgr.INST.init(appTmp);

        return true;
    }

    /**
     * 閲婃斁璧勬簮锛岃繖閲屼竴鑸笉闇�瑕佽皟鐢� | Frees resources, which are generally not required to call
     */
    public static void destroy() {
        HMSAgentLog.i("destroy HMSAgent");
        ActivityMgr.INST.release();
        ApiClientMgr.INST.release();
    }

    /**
     * 杩炴帴HMS SDK锛� 鍙兘鎷夎捣鐣岄潰(鍖呮嫭鍗囩骇寮曞绛�)锛屽缓璁湪绗竴涓晫闈㈣繘琛岃繛鎺ャ�� | Connecting to the HMS SDK may pull up the activity (including upgrade guard, etc.), and it is recommended that you connect in the first activity.
     * 姝ゆ柟娉曞彲浠ラ噸澶嶈皟鐢紝娌″繀瑕佷负浜嗗彧璋冪敤涓�娆″仛澶嶆潅澶勭悊 | This method can be called repeatedly, and there is no need to do complex processing for only one call at a time
     * 鏂规硶涓哄紓姝ヨ皟鐢紝璋冪敤缁撴灉鍦ㄤ富绾跨▼鍥炶皟 | Method is called asynchronously, and the result is invoked in the main thread callback
     * @param activity 褰撳墠鐣岄潰鐨刟ctivity锛� 涓嶈兘浼犵┖ | Activity of the current activity, cannot be empty
     * @param callback 杩炴帴缁撴灉鍥炶皟 | Connection Result Callback
     */
    public static void connect(Activity activity, final ConnectHandler callback) {
        HMSAgentLog.i("start connect");
        ApiClientMgr.INST.connect(new IClientConnectCallback() {
            @Override
            public void onConnect(final int rst, HuaweiApiClient client) {
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onConnect(rst);
                        }
                    });
                }
            }
        }, true);
    }

    /**
     * 妫�鏌ユ湰搴旂敤鐨勫崌绾� | Check for upgrades to this application
     * @param activity 涓婁笅鏂� | context
     */
    public static void checkUpdate (final Activity activity) {
        HMSAgentLog.i("start checkUpdate");
        ApiClientMgr.INST.connect(new IClientConnectCallback() {
            @Override
            public void onConnect(int rst, HuaweiApiClient client) {
                Activity activityCur = ActivityMgr.INST.getLastActivity();

                if (activityCur != null && client != null) {
                    client.checkUpdate(activityCur);
                } else if (activity != null && client != null){
                    client.checkUpdate(activity);
                } else {
                    // 璺烻E纭锛歛ctivity 涓� null 锛� 涓嶅鐞� | Activity is null and does not need to be processed
                    HMSAgentLog.e("no activity to checkUpdate");
                }
            }
        }, true);
    }





    /**
     * push鎺ュ彛灏佽 | Push interface Encapsulation
     */
    public static final class Push {
        /**
         * 鑾峰彇pushtoken鎺ュ彛 | Get Pushtoken method
         * pushtoken閫氳繃骞挎挱涓嬪彂锛岃鐩戝惉鐨勫箍鎾紝璇峰弬瑙丠MS-SDK寮�鍙戝噯澶囦腑PushReceiver鐨勬敞鍐� | Pushtoken Broadcast issued, to listen to the broadcast, see HMS-SDK Development Preparation Pushreceiver Registration
         * @param handler pushtoken鎺ュ彛璋冪敤鍥炶皟锛堢粨鏋滀細鍦ㄤ富绾跨▼鍥炶皟锛� | getToken method Call callback (result will be callback in main thread)
         */
        public static void getToken(GetTokenHandler handler){
            new GetTokenApi().getToken(handler);
        }

        /**
         * 鍒犻櫎鎸囧畾鐨刾ushtoken | Deletes the specified Pushtoken
         * 璇ユ帴鍙ｅ彧鍦‥MUI5.1浠ュ強鏇撮珮鐗堟湰鐨勫崕涓烘墜鏈轰笂璋冪敤璇ユ帴鍙ｅ悗鎵嶄笉浼氭敹鍒癙USH娑堟伅銆� | The method will not receive a push message until it is invoked on EMUI5.1 and later Huawei handsets.
         * @param token 瑕佸垹闄ょ殑token | Token to delete
         * @param handler 鏂规硶璋冪敤缁撴灉鍥炶皟锛堢粨鏋滀細鍦ㄤ富绾跨▼鍥炶皟锛� | Method call result Callback (result will be callback on main thread)
         */
        public static void deleteToken(String token, DeleteTokenHandler handler){
            new DeleteTokenApi().deleteToken(token, handler);
        }

        /**
         * 鑾峰彇push鐘舵�侊紝push鐘舵�佺殑鍥炶皟閫氳繃骞挎挱鍙戦�併�� | Gets the push state, and the push state callback is sent by broadcast.
         * 瑕佺洃鍚殑骞挎挱锛岃鍙傝HMS-SDK寮�鍙戝噯澶囦腑PushReceiver鐨勬敞鍐� | To listen for broadcasts, see Pushreceiver Registration in HMS-SDK development preparation
         * @param handler 鏂规硶璋冪敤缁撴灉鍥炶皟锛堢粨鏋滀細鍦ㄤ富绾跨▼鍥炶皟锛� | Method call result Callback (result will be callback on main thread)
         */
        public static void getPushState(GetPushStateHandler handler){
            new GetPushStateApi().getPushState(handler);
        }

        /**
         * 鎵撳紑/鍏抽棴閫氱煡鏍忔秷鎭� | Turn on/off notification bar messages
         * @param enable 鎵撳紑/鍏抽棴 | Turn ON/off
         * @param handler 鏂规硶璋冪敤缁撴灉鍥炶皟锛堢粨鏋滀細鍦ㄤ富绾跨▼鍥炶皟锛� | Method call result Callback (result will be callback on main thread)
         */
        public static void enableReceiveNotifyMsg(boolean enable, EnableReceiveNotifyMsgHandler handler){
            new EnableReceiveNotifyMsgApi().enableReceiveNotifyMsg(enable, handler);
        }

        /**
         * 鎵撳紑/鍏抽棴閫忎紶娑堟伅 | Turn on/off the pass message
         * @param enable 鎵撳紑/鍏抽棴 | Turn ON/off
         * @param handler 鏂规硶璋冪敤缁撴灉鍥炶皟锛堢粨鏋滀細鍦ㄤ富绾跨▼鍥炶皟锛� | Method call result Callback (result will be callback on main thread)
         */
        public static void enableReceiveNormalMsg(boolean enable, EnableReceiveNormalMsgHandler handler){
            new EnableReceiveNormalMsgApi().enableReceiveNormalMsg(enable, handler);
        }

        /**
         * 璇锋眰push鍗忚灞曠ず | Request Push Protocol Display
         * @param handler 鏂规硶璋冪敤缁撴灉鍥炶皟锛堢粨鏋滀細鍦ㄤ富绾跨▼鍥炶皟锛墊 Method call result Callback (result will be callback on main thread)
         */
        public static void queryAgreement(QueryAgreementHandler handler){
            new QueryAgreementApi().queryAgreement(handler);
        }
    }
}
