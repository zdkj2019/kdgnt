package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity Management Class
 * 姝ょ被娉ㄥ唽浜哸ctivity鐨勭敓鍛藉懆鏈熺洃鍚紝鐢ㄦ潵鑾峰彇鏈�鏂扮殑activity缁欏悗缁�昏緫澶勭悊浣跨敤 | This class registers the life cycle monitoring of the activity to obtain the latest activity for subsequent logical processing using
 */
public final class ActivityMgr implements Application.ActivityLifecycleCallbacks {

    /**
     * 鍗曞疄渚� | Single Instance
     */
    public static final ActivityMgr INST = new ActivityMgr();

    private static final Object LOCK_LASTACTIVITIES = new Object();

    /**
     * 搴旂敤绋嬪簭 | application
     */
    private Application application;

    /**
     * 鏈�鏂扮殑activity鍒楄〃锛屽鏋滄病鏈夊垯涓虹┖鍒楄〃 | Latest list of activity, if no, empty list
     */
    private List<Activity> curActivities = new ArrayList<Activity>();

    /**
     * activity onResume Event Monitoring
     */
    private List<IActivityResumeCallback> resumeCallbacks = new ArrayList<IActivityResumeCallback>();

    /**
     * activity onPause Event Monitoring
     */
    private List<IActivityPauseCallback> pauseCallbacks = new ArrayList<IActivityPauseCallback>();

    /**
     * activity onDestroyed Event Monitoring
     */
    private List<IActivityDestroyedCallback> destroyedCallbacks = new ArrayList<IActivityDestroyedCallback>();

    /**
     * 绉佹湁鏋勯�犳柟娉� | Private construction methods
     * 闃叉澶栭潰鐩存帴鍒涘缓瀹炰緥 | Prevent external instances from being created directly
     */
    private ActivityMgr(){}

    /**
     * 鍒濆鍖栨柟娉� | Initialization method
     * @param app 搴旂敤绋嬪簭 | application
     */
    public void init(Application app, Activity initActivity) {
        HMSAgentLog.d("init");

        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(this);
        }

        application = app;
        setCurActivity(initActivity);
        app.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 閲婃斁璧勬簮锛屼竴鑸笉闇�瑕佽皟鐢� | Frees resources, and generally does not need to call
     */
    public void release() {
        HMSAgentLog.d("release");
        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(this);
        }

        clearCurActivities();
        clearActivitResumeCallbacks();
        clearActivitPauseCallbacks();
        application = null;
    }

    /**
     * 娉ㄥ唽activity onResume浜嬩欢鍥炶皟 | Registering an Activity Onresume event Callback
     * @param callback activity onResume浜嬩欢鍥炶皟 | Activity Onresume Event Callback
     */
    public void registerActivitResumeEvent(IActivityResumeCallback callback) {
        HMSAgentLog.d("registerOnResume:" + StrUtils.objDesc(callback));
        resumeCallbacks.add(callback);
    }

    /**
     * 鍙嶆敞鍐宎ctivity onResume浜嬩欢鍥炶皟 | unregistration Activity Onresume Event Callback
     * @param callback 宸茬粡娉ㄥ唽鐨� activity onResume浜嬩欢鍥炶皟 | Registered Activity Onresume Event callback
     */
    public void unRegisterActivitResumeEvent(IActivityResumeCallback callback) {
        HMSAgentLog.d("unRegisterOnResume:" + StrUtils.objDesc(callback));
        resumeCallbacks.remove(callback);
    }

    /**
     * 娉ㄥ唽activity onPause 浜嬩欢鍥炶皟 | Registering an Activity OnPause event Callback
     * @param callback activity onPause 浜嬩欢鍥炶皟 | Activity OnPause Event Callback
     */
    public void registerActivitPauseEvent(IActivityPauseCallback callback) {
        HMSAgentLog.d("registerOnPause:" + StrUtils.objDesc(callback));
        pauseCallbacks.add(callback);
    }

    /**
     * 鍙嶆敞鍐宎ctivity onPause浜嬩欢鍥炶皟 | unregistration activity OnPause Event Callback
     * @param callback 宸茬粡娉ㄥ唽鐨� activity onPause浜嬩欢鍥炶皟 | Registered Activity OnPause Event callback
     */
    public void unRegisterActivitPauseEvent(IActivityPauseCallback callback) {
        HMSAgentLog.d("unRegisterOnPause:" + StrUtils.objDesc(callback));
        pauseCallbacks.remove(callback);
    }

    /**
     * 娉ㄥ唽activity onDestroyed 浜嬩欢鍥炶皟 | Registering an Activity ondestroyed event Callback
     * @param callback activity onDestroyed 浜嬩欢鍥炶皟 | Activity Ondestroyed Event Callback
     */
    public void registerActivitDestroyedEvent(IActivityDestroyedCallback callback) {
        HMSAgentLog.d("registerOnDestroyed:" + StrUtils.objDesc(callback));
        destroyedCallbacks.add(callback);
    }

    /**
     * 鍙嶆敞鍐宎ctivity onDestroyed 浜嬩欢鍥炶皟 | unregistration Activity ondestroyed Event Callback
     * @param callback 宸茬粡娉ㄥ唽鐨� activity onDestroyed浜嬩欢鍥炶皟 | Registered Activity ondestroyed Event callback
     */
    public void unRegisterActivitDestroyedEvent(IActivityDestroyedCallback callback) {
        HMSAgentLog.d("unRegisterOnDestroyed:" + StrUtils.objDesc(callback));
        destroyedCallbacks.remove(callback);
    }

    /**
     * 娓呯┖ activity onResume浜嬩欢鍥炶皟 | Clear Activity Onresume Event callback
     */
    public void clearActivitResumeCallbacks() {
        HMSAgentLog.d("clearOnResumeCallback");
        resumeCallbacks.clear();
    }

    /**
     * 娓呯┖ activity onPause 浜嬩欢鍥炶皟 | Clear Activity OnPause Event callback
     */
    public void clearActivitPauseCallbacks() {
        HMSAgentLog.d("clearOnPauseCallback");
        pauseCallbacks.clear();
    }

    /**
     * 鑾峰彇鏈�鏂扮殑activity | Get the latest activity
     * @return 鏈�鏂扮殑activity | Latest activity
     */
    public Activity getLastActivity() {
        return getLastActivityInner();
    }

    /**
     * activity onCreate 鐩戝惉鍥炶皟 | Activity OnCreate Listener Callback
     * @param activity 鍙戠敓onCreate浜嬩欢鐨刟ctivity | Activity that occurs OnCreate events
     * @param savedInstanceState 缂撳瓨鐘舵�佹暟鎹� | Cached state data
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        HMSAgentLog.d("onCreated:" + StrUtils.objDesc(activity));
        setCurActivity(activity);
    }

    /**
     * activity onStart 鐩戝惉鍥炶皟 | Activity OnStart Listener Callback
     * @param activity 鍙戠敓onStart浜嬩欢鐨刟ctivity | Activity that occurs OnStart events
     */
    @Override
    public void onActivityStarted(Activity activity) {
        HMSAgentLog.d("onStarted:" + StrUtils.objDesc(activity));
        setCurActivity(activity);
    }

    /**
     * activity onResume 鐩戝惉鍥炶皟 | Activity Onresume Listener Callback
     * @param activity 鍙戠敓onResume浜嬩欢鐨刟ctivity | Activity that occurs Onresume events
     */
    @Override
    public void onActivityResumed(Activity activity) {
        HMSAgentLog.d("onResumed:" + StrUtils.objDesc(activity));
        setCurActivity(activity);

        List<IActivityResumeCallback> tmdCallbacks = new ArrayList<IActivityResumeCallback>(resumeCallbacks);
        for (IActivityResumeCallback callback : tmdCallbacks) {
            callback.onActivityResume(activity);
        }
    }

    /**
     * activity onPause 鐩戝惉鍥炶皟 | Activity OnPause Listener Callback
     * @param activity 鍙戠敓onPause浜嬩欢鐨刟ctivity | Activity that occurs OnPause events
     */
    @Override
    public void onActivityPaused(Activity activity) {
        HMSAgentLog.d("onPaused:" + StrUtils.objDesc(activity));
        List<IActivityPauseCallback> tmdCallbacks = new ArrayList<IActivityPauseCallback>(pauseCallbacks);
        for (IActivityPauseCallback callback : tmdCallbacks) {
            callback.onActivityPause(activity);
        }
    }

    /**
     * activity onStop 鐩戝惉鍥炶皟 | Activity OnStop Listener Callback
     * @param activity 鍙戠敓onStop浜嬩欢鐨刟ctivity | Activity that occurs OnStop events
     */
    @Override
    public void onActivityStopped(Activity activity) {
        HMSAgentLog.d("onStopped:" + StrUtils.objDesc(activity));
    }

    /**
     * activity onSaveInstanceState 鐩戝惉鍥炶皟 | Activity Onsaveinstancestate Listener Callback
     * @param activity 鍙戠敓 onSaveInstanceState 浜嬩欢鐨刟ctivity | Activity that occurs onsaveinstancestate events
     */
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    /**
     * activity onDestroyed 鐩戝惉鍥炶皟 | Activity Ondestroyed Listener Callback
     * @param activity 鍙戠敓 onDestroyed 浜嬩欢鐨刟ctivity | Activity that occurs ondestroyed events
     */
    @Override
    public void onActivityDestroyed(Activity activity) {
        HMSAgentLog.d("onDestroyed:" + StrUtils.objDesc(activity));
        removeActivity(activity);

        // activity onDestroyed 浜嬩欢鍥炶皟 | Activity Ondestroyed Event Callback
        List<IActivityDestroyedCallback> tmdCallbacks = new ArrayList<IActivityDestroyedCallback>(destroyedCallbacks);
        for (IActivityDestroyedCallback callback : tmdCallbacks) {
            callback.onActivityDestroyed(activity, getLastActivityInner());
        }
    }

    /**
     * 绉婚櫎褰撳墠activity | Remove Current Activity
     * @param curActivity 瑕佺Щ闄ょ殑activity | Activity to remove
     */
    private void removeActivity(Activity curActivity) {
        synchronized (LOCK_LASTACTIVITIES) {
            curActivities.remove(curActivity);
        }
    }

    /**
     * 璁剧疆鏈�鏂扮殑activity | Set up the latest activity
     * @param curActivity 鏈�鏂扮殑activity | Latest activity
     */
    private void setCurActivity(Activity curActivity) {
        synchronized (LOCK_LASTACTIVITIES) {
            int idxCurActivity = curActivities.indexOf(curActivity);
            if (idxCurActivity == -1) {
                curActivities.add(curActivity);
            } else if (idxCurActivity < curActivities.size()-1){
                curActivities.remove(curActivity);
                curActivities.add(curActivity);
            }
        }
    }

    /**
     * 鑾峰彇鏈�鏂扮殑activity锛屽鏋滄病鏈夊垯杩斿洖null | Gets the latest activity and returns null if not
     * @return 鏈�鏂扮殑activity | Latest activity
     */
    private Activity getLastActivityInner(){
        synchronized (LOCK_LASTACTIVITIES) {
            if (curActivities.size() > 0) {
                return curActivities.get(curActivities.size()-1);
            } else {
                return null;
            }
        }
    }

    /**
     * 娓呯悊activities | Clean activities
     */
    private void clearCurActivities(){
        synchronized (LOCK_LASTACTIVITIES) {
            curActivities.clear();
        }
    }
}