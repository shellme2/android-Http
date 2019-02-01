package com.example.http.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.eebbk.bfc.http.BfcHttp;
import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.eebbk.bfc.http.config.BfcRequestConfigure;
import com.eebbk.bfc.http.error.BfcHttpError;
import com.eebbk.bfc.http.toolbox.IBfcErrorListener;
import com.eebbk.bfc.http.toolbox.StringCallBack;
import com.eebbk.bfc.http.tools.UserPreferences;
import com.example.http.R;
import com.example.http.bean.TerminalMsg;
import com.example.http.databinding.ActivityBfcHttpRequestTestBinding;
import com.example.http.toolbox.NetWorkUtils;

import java.util.HashMap;
import java.util.Map;

public class BfcHttpRequestTestActivity extends Activity {

    private static final String TAG = "BFC_HTTP";

    private static final String getUrl = "http://119.29.29.29/d?dn=D83772A6AA837E1F51AADF854E1AE965&id=216&ttl=1";
    private static final String getUrlWithParam = "http://test.eebbk.net/onlinelessonM/appCoursePackage/getCoursePackageById?coursePackageId=1004";
    private Map<String, String> getParam = new HashMap<>();

    private static final String postUrl = "http://test.eebbk.net/social-scan/searcher/Switch/getKeySwitch";
    private Map<String, String> postParam = new HashMap<>();

    private static final String fakeUrl = "https://www.baidu.com/";

    private ToggleButton httpToggle;
    private ToggleButton cacheToggle;

    private long funStartTime;

    private EditText etTimeOut;
    private EditText expireTime;

    private ActivityBfcHttpRequestTestBinding binding;

    private TerminalMsg terminalMsg = new TerminalMsg();

    private int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bfc_http_request_test);
        binding.setTerminalMsg(terminalMsg);

        httpToggle = (ToggleButton) findViewById(R.id.http_toggle);
        cacheToggle = (ToggleButton) findViewById(R.id.cache_toggle);

        etTimeOut = (EditText) findViewById(R.id.timeOut);
        expireTime = (EditText) findViewById(R.id.cacheExpireTime);

        httpToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                httpToggle.setChecked(isChecked);
                BfcHttpConfigure.setEnableDNSAntiHijack(isChecked);
            }
        });

        getParam.put("coursePackageId", "1004");
        postParam.put("machineId", "2");
        postParam.put("versionCode", "3");
        postParam.put("devicemodel", "GET");

        BfcHttpConfigure.setEnableDNSAntiHijack(httpToggle.isChecked());
    }

    private void doOnResponse(String response, String url, Map<String, String> params) {
        Log.d(TAG, "doOnResponse: " + response);
        updateMessage(response, url, params);
    }

    private void doOnError(BfcHttpError error, String url, Map<String, String> params) {
        Log.d(TAG, "onError: " + error.getMessage());
        updateMessage(error.getMessage() + "\nstatus:" + error.getErrorCode(), url, params);
    }

    public void processGet(View view) {
        funStartTime = System.currentTimeMillis();
        terminalMsg.setMsg("网络请求运行中...");
        binding.setTerminalMsg(terminalMsg);
//        NetWorkUtils.get(getApplicationContext(), getUrlWithParam, null, new StringCallBack() {
//            @Override
//            public void onResponse(String response) {
//                doOnResponse(response, getUrlWithParam, null);
//            }
//        }, new IBfcErrorListener() {
//            @Override
//            public void onError(BfcHttpError error) {
//                doOnError(error, getUrlWithParam, null);
//            }
//        });
        try {
            time = Integer.parseInt(expireTime.getText().toString());
        } catch (NumberFormatException e) {
            time = 0;
            e.printStackTrace();
        }

        BfcHttp.get(getApplicationContext(), getUrlWithParam, null, new BfcRequestConfigure.Builder().setExpiredTime(time * 1000).setCacheAble(cacheToggle.isChecked()).build(), new StringCallBack() {
            @Override
            public void onResponse(String response) {
                doOnResponse(response + "\n\n 缓存有效时长" + time + "秒", postUrl, postParam);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                doOnError(error, postUrl, postParam);
            }
        });


        Map<String, String> params = new HashMap<>();
        params.put("devicemodel", "S1");
        params.put("deviceosversion", "V1.40_161206");
        params.put("machineId", "700S107030842");
        params.put("versionName", "2.5.1");
        params.put("packageName", "com.eebbk.hanziLearning.activity");
        params.put("versionCode", "18");

        Map<String, String> header = new HashMap<>();
        header.put("deviceModel", "S1");
        header.put("apkVersionCode", "18");
        header.put("deviceOSVersion", "V1.40_161206");
        header.put("apkPackageName", "com.eebbk.hanziLearning.activity");
        header.put("machineId", "700S107030842");
        header.put("accountId", "神童升级");

        BfcHttp.post(this, "http://jiajiaoji.eebbk.net/appUpdate/apkInfo/getNewApkInfo", params, header, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse:/*************************************************************** " + response);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                Log.d(TAG, "onError: **********************************************************" + error.getMsg());
            }
        });
    }

    public void processGetWithParam(View view) {
        funStartTime = System.currentTimeMillis();
        terminalMsg.setMsg("网络请求运行中...");
        binding.setTerminalMsg(terminalMsg);
        NetWorkUtils.get(getApplicationContext(), getUrl, getParam, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                doOnResponse(response, getUrl, getParam);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                doOnError(error, getUrl, getParam);
            }
        });
    }

    public void processPost(View view) {
        funStartTime = System.currentTimeMillis();
        terminalMsg.setMsg("网络请求运行中...");
        binding.setTerminalMsg(terminalMsg);
//        NetWorkUtils.post(getApplicationContext(), postUrl, postParam, new StringCallBack() {
//            @Override
//            public void onResponse(String response) {
//                doOnResponse(response, postUrl, postParam);
//            }
//        }, new IBfcErrorListener() {
//            @Override
//            public void onError(BfcHttpError error) {
//                doOnError(error, postUrl, postParam);
//            }
//        });
        try {
            time = Integer.parseInt(expireTime.getText().toString());
        } catch (NumberFormatException e) {
            time = 0;
            e.printStackTrace();
        }
        BfcHttp.post(getApplicationContext(), postUrl, postParam, new BfcRequestConfigure.Builder().setExpiredTime(time * 1000).setCacheAble(cacheToggle.isChecked()).build(), new StringCallBack() {
            @Override
            public void onResponse(String response) {
                doOnResponse(response + "\n\n 缓存有效时长" + time + "秒", postUrl, postParam);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                doOnError(error, postUrl, postParam);
            }
        });
    }

    public void processPut(View view) {
        funStartTime = System.currentTimeMillis();
        updateMessage("当前版本未实现PUT请求类型", "", null);
    }

    public void processDelete(View view) {
        funStartTime = System.currentTimeMillis();
        updateMessage("当前版本未实现DELETE请求类型", "", null);
    }

    public void processFakeGet(View view) {
        funStartTime = System.currentTimeMillis();
        terminalMsg.setMsg("网络请求运行中...");
        binding.setTerminalMsg(terminalMsg);
        NetWorkUtils.get(getApplicationContext(), fakeUrl, null, new StringCallBack() {
            @Override
            public void onResponse(String response) {
                doOnResponse(response, fakeUrl, null);
            }
        }, new IBfcErrorListener() {
            @Override
            public void onError(BfcHttpError error) {
                doOnError(error, fakeUrl, null);
            }
        });
    }

    public void clearAllCache(View view) {
        funStartTime = System.currentTimeMillis();
        BfcHttp.clearCache();
        updateMessage("清除全部URL缓存", "", null);
    }

    public void clearUrlCache(View view) {
        funStartTime = System.currentTimeMillis();
        BfcHttp.clearCache(postUrl);
        updateMessage("清除特定URL缓存", postUrl, null);
    }

    public void getCacheDir(View view) {
        funStartTime = System.currentTimeMillis();
        BfcHttp.getCacheDir(this);
        updateMessage("获取缓存目录: \n" + BfcHttp.getCacheDir(this).getAbsolutePath() + "\n" + BfcHttp.getCacheDir(this).getTotalSpace(), "", null);
    }

    public void clearPreferences(View view) {
        funStartTime = System.currentTimeMillis();
        UserPreferences.clear(getApplicationContext());
        updateMessage("清除缓存IP", "", null);
    }

    public void setTimeOut(View view) {
        funStartTime = System.currentTimeMillis();
        int timeOut = 1000;
        try {
            if (TextUtils.isEmpty(etTimeOut.getText())) {
                return;
            }
            timeOut = Integer.parseInt(etTimeOut.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        BfcHttpConfigure.setTimeout(timeOut);
        updateMessage("设置超时时长", "", null);
    }

    public void processNoStopGet(View view) {
        final int count;
        try {
            count = Integer.parseInt(((EditText) findViewById(R.id.noStopCount)).getText().toString());
        } catch (NumberFormatException e) {
            updateMessage("循环次数设置有误", "", null);
            return;
        }
        isStop = false;
        funStartTime = 0;
        terminalMsg.setMsg("网络请求无线循环运行中...");
        binding.setTerminalMsg(terminalMsg);
        new Thread() {
            @Override
            public void run() {
                int times = count == 0 ? Integer.MAX_VALUE : count;
                while (!isStop && times > 0) {
                    NetWorkUtils.get(getApplicationContext(), getUrlWithParam, null, new StringCallBack() {
                        @Override
                        public void onResponse(String response) {
                            doOnResponse(response, getUrlWithParam, null);
                        }
                    }, new IBfcErrorListener() {
                        @Override
                        public void onError(BfcHttpError error) {
                            doOnError(error, getUrlWithParam, null);
                        }
                    });
                    times--;
                }
            }
        }.start();
    }

    public void processNoStopPost(View view) {
        final int count;
        try {
            count = Integer.parseInt(((EditText) findViewById(R.id.noStopCount)).getText().toString());
        } catch (NumberFormatException e) {
            updateMessage("循环次数设置有误", "", null);
            return;
        }
        isStop = false;
        funStartTime = 0;
        terminalMsg.setMsg("网络请求无线循环运行中...");
        binding.setTerminalMsg(terminalMsg);
        new Thread() {
            @Override
            public void run() {
                int times = count == 0 ? Integer.MAX_VALUE : count;
                while (!isStop && times > 0) {
                    NetWorkUtils.post(getApplicationContext(), postUrl, postParam, new StringCallBack() {
                        @Override
                        public void onResponse(String response) {
                            doOnResponse(response, postUrl, postParam);
                        }
                    }, new IBfcErrorListener() {
                        @Override
                        public void onError(BfcHttpError error) {
                            doOnError(error, postUrl, postParam);
                        }
                    });
                    times--;
                }
            }
        }.start();
    }

    private boolean isStop = false;

    public void stopRequest(View view) {
        isStop = true;
        terminalMsg.setMsg("停止无线循环请求");
        binding.setTerminalMsg(terminalMsg);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BfcHttp.cancelAll();
    }

    private void updateMessage(String msg, String url, Map<String, String> params) {
        long funTime = System.currentTimeMillis() - funStartTime;
        StringBuilder builder = new StringBuilder();
        builder.append("测试信息:  ");
        builder.append((BfcHttpConfigure.isEnableDNSAntiHijack() ? "反劫持网络请求" : "普通网络请求"));
        builder.append(cacheToggle.isChecked() ? "禁止缓存" : "允许缓存");
        builder.append(funStartTime == 0 ? "\n当前请求响应时间：" : "\n请求耗时：");
        builder.append(funTime).append(funStartTime == 0 ? "\n" : "ms\n");
        builder.append("\n请求URL：").append(url).append("\n");
        builder.append("\n请求参数：").append(params).append("\n\n");
        builder.append(msg);

        terminalMsg.setMsg(builder.toString());
        binding.setTerminalMsg(terminalMsg);
        funStartTime = 0;
    }
}
