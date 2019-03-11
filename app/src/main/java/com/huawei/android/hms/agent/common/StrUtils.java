package com.huawei.android.hms.agent.common;

/**
 * 宸ュ叿绫�
 */
public final class StrUtils {
    /**
     * 杩斿洖瀵硅薄鐨勬弿杩帮紝杩欓噷涓轰簡閬垮厤鐢ㄦ埛鏁版嵁闅愮鐨勬硠闇诧紝鍙槸杩斿洖瀵硅薄鏈韩鐨勬弿杩� 绫诲悕@hashcode
     * @param object 瀵硅薄
     * @return 瀵硅薄鐨勬弿杩�
     */
    public static String objDesc(Object object) {
        return object == null ? "null" : (object.getClass().getName()+'@'+Integer.toHexString(object.hashCode()));
    }
}
