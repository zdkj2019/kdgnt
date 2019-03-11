package com.huawei.android.hms.agent.common;

import android.app.Activity;

/**
 * Activity onResume 浜嬩欢鍥炶皟鎺ュ彛
 */
public interface IActivityResumeCallback {

    /**
     * Activity onResume鍥炶皟
     * @param activity 鍙戠敓 onResume 浜嬩欢鐨刟ctivity
     */
    void onActivityResume(Activity activity);
}
