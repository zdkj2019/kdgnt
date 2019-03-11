package com.huawei.android.hms.agent.common;

import android.util.Log;

/**
 * 鏃ュ織鎵撳嵃绫伙紝瀵规墦鍗版棩蹇楄繘琛屽皝瑁咃紝鏂逛究鏍规嵁鏃ュ織瀹氫綅闂
 */
public final class HMSAgentLog {

    /**
     * 鏃ュ織鍥炶皟锛屽皢瑕佹墦鍗扮殑鏃ュ織鍥炶皟缁欏紑鍙戣�咃紝鐢卞紑鍙戣�呭皢鏃ュ織杈撳嚭
     */
    public interface IHMSAgentLogCallback {
        void logD(String tag, String log);
        void logV(String tag, String log);
        void logI(String tag, String log);
        void logW(String tag, String log);
        void logE(String tag, String log);
    }

    private static final int START_STACK_INDEX = 4;
    private static final int PRINT_STACK_COUTN = 2;

    private static IHMSAgentLogCallback logCallback = null;

    public static void setHMSAgentLogCallback(IHMSAgentLogCallback callback){
        logCallback = callback;
    }

    public static void d(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);

        if (logCallback != null) {
            logCallback.logD("HMSAgent", sb.toString());
        } else {
            Log.d("HMSAgent", sb.toString());
        }
    }

    public static void v(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if (logCallback != null) {
            logCallback.logV("HMSAgent", sb.toString());
        } else {
            Log.v("HMSAgent", sb.toString());
        }
    }

    public static void i(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if (logCallback != null) {
            logCallback.logI("HMSAgent", sb.toString());
        } else {
            Log.i("HMSAgent", sb.toString());
        }
    }

    public static void w(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if (logCallback != null) {
            logCallback.logW("HMSAgent", sb.toString());
        } else {
            Log.w("HMSAgent", sb.toString());
        }
    }

    public static void e(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);

        if (logCallback != null) {
            logCallback.logE("HMSAgent", sb.toString());
        } else {
            Log.e("HMSAgent", sb.toString());
        }
    }

    private static void appendStack(StringBuilder sb) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stacks != null && stacks.length > START_STACK_INDEX) {
            int lastIndex = Math.min(stacks.length-1,START_STACK_INDEX+PRINT_STACK_COUTN);
            for (int i=lastIndex; i >= START_STACK_INDEX; i--) {
                if (stacks[i] == null) {
                    continue;
                }

                String fileName = stacks[i].getFileName();
                if (fileName != null) {
                    int dotIndx = fileName.indexOf('.');
                    if (dotIndx > 0) {
                        fileName = fileName.substring(0, dotIndx);
                    }
                }

                sb.append(fileName);
                sb.append('(');
                sb.append(stacks[i].getLineNumber());
                sb.append(")");
                sb.append("->");
            }
            sb.append(stacks[START_STACK_INDEX].getMethodName());
        }
        sb.append('\n');
    }
}
