package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 鍩虹activity锛岀敤鏉ュ鐞嗗叕鍏辩殑閫忔槑鍙傛暟
 */
public class BaseAgentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestActivityTransparent();
    }

    /**
     * 鍚敤閫忔槑鐨勮烦鏉緼ctivity
     */
    private void requestActivityTransparent() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        if (window != null) {
            window.addFlags(0x04000000);
        }
    }
}
