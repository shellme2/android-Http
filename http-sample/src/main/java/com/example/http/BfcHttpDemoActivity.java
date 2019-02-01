package com.example.http;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eebbk.bfc.http.SDKVersion;
import com.eebbk.bfc.http.config.BfcHttpConfigure;
import com.example.http.activity.BfcHttpCustomRequestTestActivity;
import com.example.http.activity.BfcHttpDnsTestActivity;
import com.example.http.activity.BfcHttpRequestTestActivity;
import com.example.http.activity.BfcHttpsTestActivity;
import com.example.http.activity.RequestConfTestActivity;

public class BfcHttpDemoActivity extends Activity {
    private boolean mSwitch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfc_http_demo);
        TextView mBuildTimeTv = (TextView) findViewById(R.id.buildTime);
        StringBuilder builder = new StringBuilder();
        builder.append("版本信息：\n");
        builder.append("版本名：").append(SDKVersion.getVersionName()).append("\n");
        builder.append("构建信息").append(SDKVersion.getBuildName()).append("\n");
        builder.append("构建时间：").append(SDKVersion.getBuildTime()).append("\n");
        builder.append("版本号：").append(SDKVersion.getSDKInt());
        mBuildTimeTv.setText(builder);
    }

    public void enterRequestTest(View view) {
        Intent intent = new Intent(this, BfcHttpRequestTestActivity.class);
        startActivity(intent);
    }

    public void enterCustomTest(View view) {
        Intent intent = new Intent(this, BfcHttpCustomRequestTestActivity.class);
        startActivity(intent);
    }

    public void enterRequestConfTest(View view) {
        Intent intent = new Intent(this, RequestConfTestActivity.class);
        startActivity(intent);
    }

    public void enterHttpsTest(View view) {
        Intent intent = new Intent(this, BfcHttpsTestActivity.class);
        startActivity(intent);
    }
    public void enterHttpDnsTest(View view) {
        Intent intent = new Intent(this, BfcHttpDnsTestActivity.class);
        startActivity(intent);
    }

    public void switchReported(View view) {
//        if (mSwitch) {
//            BfcHttpConfigure.setReportedFlag(false);
//            mSwitch = false;
//            ((Button) view).setText("打开上报开关");
//        } else {
//            BfcHttpConfigure.setReportedFlag(true);
//            mSwitch = true;
//            ((Button) view).setText("关闭上报开关");
//        }
    }
}
