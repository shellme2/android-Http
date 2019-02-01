package com.example.http;

import android.app.Application;
import android.os.StrictMode;

import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.example.http.activity.AppContext;
import com.example.http.toolbox.CustomHttpRequest;
import com.example.http.toolbox.CustomHttpStatusWatch;
import com.example.http.toolbox.OkHttp3Stack;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by lzy on 2016/10/5.
 */

public class DemoApplication extends Application {
    public static DemoApplication instance;
    @Override
    public void onCreate() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .penaltyDeath()
//                .build());
        super.onCreate();

        BfcHttpConfigure.init(this, new OkHttp3Stack());
        BfcHttpConfigure.openDebug(true);
        BfcHttpConfigure.setDomain("test.eebbk.net");

        BfcHttpConfigure.setBfcHttpStatusWatch(new CustomHttpStatusWatch());

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        BlockCanary.install(this, new AppContext()).start();
        instance = this;
    }

    public static DemoApplication getAppContext() {
        return instance;
    }

    private void bfcHttpConfig() {
        //初始化配置
        BfcHttpConfigure.init(this);
        //开启调试模式，打印详细日志信息
        BfcHttpConfigure.openDebug(true);
        //全局设置是否开启缓存，允许每个请求通过BfcRequestConfigure单独设置
        BfcHttpConfigure.setShouldCache(true);
        //超时时长配置，默认时长设置为5000ms
        BfcHttpConfigure.setTimeout(10000);
        //提供根据不同网络状态设置不同超时时长的配置，不配置默认使用单个时长设置，默认5000ms
        BfcHttpConfigure.setTimeout(5000, 7500, 1000);
        //设置需要执行反劫持的域名，目前仅支持设置单个域名
        BfcHttpConfigure.setDomain("test.eebbk.net");
        //实现IBfcHttpRequest接口允许自定义网络请求实现类
        BfcHttpConfigure.setCustomHttpRequest(new CustomHttpRequest());
        //网络请求过程监听类，仅实现监听开始请求，请求结束可以再callback中监听
        BfcHttpConfigure.setBfcHttpStatusWatch(new CustomHttpStatusWatch());
    }
}
