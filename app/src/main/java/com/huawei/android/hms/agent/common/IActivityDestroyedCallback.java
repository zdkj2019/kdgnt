package com.huawei.android.hms.agent.common;

import android.app.Activity;

/**
 * Activity onDestroyed 浜嬩欢鍥炶皟鎺ュ彛
 */
public interface IActivityDestroyedCallback {

    /**
     * Activity onPause鍥炶皟
     * @param activityDestroyed 鍙戠敓 onDestroyed 浜嬩欢鐨刟ctivity
     * @param activityNxt 涓嬩釜瑕佹樉绀虹殑activity
     */
    void onActivityDestroyed(Activity activityDestroyed, Activity activityNxt);
}
