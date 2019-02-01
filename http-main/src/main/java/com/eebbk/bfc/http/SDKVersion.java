package com.eebbk.bfc.http;


import com.eebbk.bfc.http.version.Build;

/**
 * 版本信息获取类
 */
public class SDKVersion {

    private SDKVersion() {
    }

    /**
     * 获取库名称
     *
     * @return 库名称
     */
    public static String getLibraryName() {
        return Build.LIBRARY_NAME;
    }

    /**
     * 构建时的版本值，如：1, 2, 3, ...
     */
    public static int getSDKInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 版本名称，如：1.0.0, 2.1.2-alpha, ...
     */
    public static String getVersionName() {
        return Build.VERSION.VERSION_NAME;
    }

    /**
     * 构建版本以及时间，主要从git获取,由GIT_TAG + "_" + GIT_SHA + "_" + BUILD_TIME组成
     */
    public static String getBuildName() {
        return Build.BUILD_NAME;
    }

    /**
     * 构建时间
     */
    public static String getBuildTime() {
        return Build.BUILD_TIME;
    }

    /**
     * 构建时的git 标签
     */
    public static String getBuildTag() {
        return Build.GIT_TAG;
    }

    /**
     * 构建时的git HEAD值
     */
    public static String getBuildHead() {
        return Build.GIT_HEAD;
    }
}
