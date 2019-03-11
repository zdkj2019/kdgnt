package com.huawei.android.hms.agent.common;

import android.app.Activity;

/**
 * Activity onPause 浜嬩欢鍥炶皟鎺ュ彛
 */
public interface IActivityPauseCallback {

    /**
     * Activity onPause鍥炶皟
     * @param activity 鍙戠敓 onPause 浜嬩欢鐨刟ctivity
     */
    void onActivityPause(Activity activity);
}
