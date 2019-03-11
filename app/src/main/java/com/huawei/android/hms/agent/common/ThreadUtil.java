package com.huawei.android.hms.agent.common;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 绾跨▼宸ュ叿锛岀敤浜庢墽琛岀嚎绋嬬瓑
 */
public final class ThreadUtil {
    public static final ThreadUtil INST = new ThreadUtil();

    private ExecutorService executors;

    private ThreadUtil(){
    }

    /**
     * 鍦ㄧ嚎绋嬩腑鎵ц
     * @param runnable 瑕佹墽琛岀殑runnable
     */
    public void excute(Runnable runnable) {
        ExecutorService executorService = getExecutorService();
        if (executorService != null) {
            // 浼樺厛浣跨敤绾跨▼姹狅紝鎻愰珮鏁堢巼
            executorService.execute(runnable);
        } else {
            // 绾跨▼姹犺幏鍙栧け璐ワ紝鍒欑洿鎺ヤ娇鐢ㄧ嚎绋�
            new Thread(runnable).start();
        }
    }

    /**
     * 鍦ㄤ富绾跨▼涓墽琛�
     * @param runnable 瑕佹墽琛岀殑runnable
     */
    public void excuteInMainThread(Runnable runnable){
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    /**
     * 鑾峰彇缂撳瓨绾跨▼姹�
     * @return 缂撳瓨绾跨▼姹犳湇鍔�
     */
    private ExecutorService getExecutorService(){
        if (executors == null) {
            try {
                executors = Executors.newCachedThreadPool();
            } catch (Exception e) {
                HMSAgentLog.e("create thread service error:" + e.getMessage());
            }
        }

        return executors;
    }
}
